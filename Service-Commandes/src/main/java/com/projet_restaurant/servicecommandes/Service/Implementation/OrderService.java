package com.projet_restaurant.servicecommandes.Service.Implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.projet_restaurant.servicecommandes.Dto.OrderDto;
import com.projet_restaurant.servicecommandes.Dto.OrderItemDto;
import com.projet_restaurant.servicecommandes.Entity.Order;
import com.projet_restaurant.servicecommandes.Entity.OrderItem;
import com.projet_restaurant.servicecommandes.Entity.OrderStatus;
import com.projet_restaurant.servicecommandes.Entity.PaymentStatusMessage;
import com.projet_restaurant.servicecommandes.Repository.OrderRepository;
import com.projet_restaurant.servicecommandes.Repository.OrderItemRepository;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private final RabbitTemplate rabbitTemplate;

    public OrderService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // Créer une nouvelle commande pour un utilisateur
    @Transactional
    public Order createOrder(Long userId, List<OrderItem> items) {
        System.out.println("Début de la création de commande pour userId: " + userId);

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);
        System.out.println("Nouvelle commande initialisée avec statut: " + order.getStatus());

        List<OrderItem> managedItems = new ArrayList<>();
        double total = 0; // Initialisation du total

        for (OrderItem item : items) {
            System.out.println("Traitement de l'item: " + item.getProductName());

            OrderItem newItem = new OrderItem(); // Crée une nouvelle instance
            newItem.setIdProduct(item.getIdProduct());
            newItem.setProductName(item.getProductName());
            newItem.setPrice(item.getPrice());
            newItem.setQuantity(item.getQuantity());
            // Utiliser le setter pour définir l'image à partir de Base64
            if (item.getImage() != null) {
                newItem.setImage(item.getImage()); // Vous stockez l'image en binaire
            }
            newItem.setOrder(order); // Associe à la commande
            managedItems.add(newItem);
// Calcul du total pour chaque item
            total += item.getPrice() * item.getQuantity();
            System.out.println("Item créé et associé à la commande: " + newItem.getProductName());
        }

        order.setItems(managedItems);
        order.setTotal(total); // On assigne le total calculé à l'entité Order
        System.out.println("Tous les items ont été associés à la commande. Total items: " + managedItems.size());

        // Sauvegarde la commande avec ses items
        Order savedOrder = orderRepository.save(order);
        System.out.println("Commande sauvegardée avec ID: " + savedOrder.getId());

        // Publier la commande dans RabbitMQ ou autres opérations
        envoyerCommande(savedOrder);
        System.out.println("Commande publiée pour traitement asynchrone.");

        return savedOrder;
    }


    // Ajouter un article à une commande existante
    @Transactional
    public Order addItemToOrder(Long userId, Long orderId, OrderItem item) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized user");
        }

        order.getItems().add(item);
        return orderRepository.save(order);
    }

    // Marquer la commande comme payée
    @Transactional
    public Order payForOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.PAID);
        return orderRepository.save(order);
    }

    // Récupérer une commande
    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

//Recuperer toutes les commandes
    /*
public List<Order> getAllOrdersWithItems() {
    System.out.println("Récupération de toutes les commandes avec leurs items.");

    // Récupérer toutes les commandes
    List<Order> orders = orderRepository.findAll(); // Assurez-vous que cette méthode est bien définie dans votre repository

    // Itérer sur chaque commande pour récupérer ses items (normalement géré par JPA)
    for (Order order : orders) {
        System.out.println("Commande ID: " + order.getId() + " avec statut: " + order.getStatus());

        // Vous pouvez directement récupérer les items si vous avez une relation @OneToMany ou @ManyToOne définie
        List<OrderItem> items = order.getItems(); // JPA charge les items associés à la commande automatiquement si la relation est bien définie
        System.out.println("Nombre d'items dans la commande " + order.getId() + ": " + items.size());

        for (OrderItem item : items) {
            System.out.println("Item ID: " + item.getId() + " - Produit: " + item.getProductName() + " - Quantité: " + item.getQuantity() + " - Prix: " + item.getPrice());
        }
    }

    return orders;
}
*/
@Bulkhead(name = "commandeService", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "getAllOrdersWithItemsBulkheadFallback")
@RateLimiter(name = "commandeService", fallbackMethod = "getAllOrdersWithItemsFallback")
@Transactional
public List<OrderDto> getAllOrdersWithItems() {
    List<Order> orders = orderRepository.findAllWithItems();  // Récupère toutes les commandes avec leurs items
    return orders.stream()
            .map(OrderDto::new)  // Convertir chaque Order en OrderDto
            .collect(Collectors.toList());
}
    // fallback for RateLimiter
    public List<OrderDto> getAllOrdersWithItemsFallback(Throwable throwable) {
        // Gérer le fallback ici, comme retourner une liste vide ou des données par défaut
        System.err.println("RateLimiter activé. Fallback appelé : " + throwable.getMessage());
        return Collections.emptyList();
    }
    // Fallback pour Bulkhead
    public List<OrderDto> getAllOrdersWithItemsBulkheadFallback(Throwable throwable) {
        System.err.println("Bulkhead activé. Fallback appelé : " + throwable.getMessage());
        return Collections.emptyList();
    }
    public void envoyerCommande(Order commande) {
        System.out.println("Envoi de la commande à RabbitMQ : " + commande);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // Ajouter le module pour gérer LocalDateTime

        try {
            // Convertir la commande en JSON
            String commandeJson = objectMapper.writeValueAsString(commande);

            // Envoi à RabbitMQ
            rabbitTemplate.convertAndSend("commande.queue", commandeJson);
            System.out.println("Commande envoyée avec succès.");
        } catch (JsonProcessingException e) {
            // Log de l'erreur et gestion fine de l'exception
            System.err.println("Erreur de conversion de la commande en JSON : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la conversion de la commande en JSON", e);
        } catch (Exception e) {
            // Gestion des erreurs RabbitMQ (si RabbitMQ est inaccessible par exemple)
            System.err.println("Erreur lors de l'envoi de la commande à RabbitMQ : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'envoi de la commande à RabbitMQ", e);
        }
    }

    @RabbitListener(queues = "payment_status.queue")
    public void listenPaymentStatus(String message) {
        try {
            System.out.println("Début de la consommation du message RabbitMQ..."); // Message de début

            // Désérialisation du message
            ObjectMapper objectMapper = new ObjectMapper();
            PaymentStatusMessage paymentStatusMessage = objectMapper.readValue(message, PaymentStatusMessage.class);

            // Afficher les données reçues du message pour vérifier
            System.out.println("Message reçu: " + message);
            System.out.println("Données du message: " + paymentStatusMessage);

            // Vérifier si la commande existe
            Optional<Order> orderOptional = orderRepository.findById(paymentStatusMessage.getCommandeId());
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                System.out.println("Commande trouvée: " + order.getId());

                // Mettre à jour le statut de la commande
                order.setStatus(OrderStatus.PAID); // Mettre à jour le statut de la commande
                orderRepository.save(order); // Sauvegarder la commande mise à jour
                System.out.println("Commande " + order.getId() + " mise à jour à 'PAID'");
            } else {
                System.out.println("Commande non trouvée pour l'ID: " + paymentStatusMessage.getCommandeId());
            }

            System.out.println("Fin de la consommation du message RabbitMQ."); // Message de fin
        } catch (Exception e) {
            System.err.println("Erreur lors de la consommation du message RabbitMQ : " + e.getMessage());
        }
    }

}
