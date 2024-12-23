package com.projet_restaurant.servicecommandes.Web;

import com.projet_restaurant.servicecommandes.Dto.OrderDto;
import com.projet_restaurant.servicecommandes.Dto.OrderItemDto;
import com.projet_restaurant.servicecommandes.Dto.OrderRequest;
import com.projet_restaurant.servicecommandes.Entity.Order;
import com.projet_restaurant.servicecommandes.Entity.OrderItem;
import com.projet_restaurant.servicecommandes.Service.Implementation.OrderService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@OpenAPIDefinition(
        info = @Info(
                title = "Service Utilisateur",
                description = " Gerer des utilisateurs",
                version = "1.0.0"
        ),

        servers = @Server(
                url = "http://localhost:8082/"
        )
)
public class OrderAPI {
    @Autowired
    private OrderService orderService;
    @Operation(
            summary = "Ajouter Une commande",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "Application/json",
                            schema = @Schema(implementation = Order.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "ajouter par succéses",
                            content = @Content(
                                    mediaType = "Application/json",
                                    schema = @Schema(implementation = Order.class))
                    ),

                    @ApiResponse(responseCode = "400",description = "erreur données"),
                    @ApiResponse(responseCode ="500", description = "erreur server")
            }
    )

    // Créer une commande pour un utilisateur
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestParam Long userId , @RequestBody OrderRequest orderRequest) {
        // Affichage dans la console des données reçues
        System.out.println("Requête reçue pour la création de commande - userId: " + userId);
        System.out.println("Items de la commande: " + orderRequest.getItems());

        // Appel au service pour créer la commande
        Order order = orderService.createOrder(userId, orderRequest.getItems());

        // Affichage dans la console de la commande créée
        System.out.println("Commande créée avec succès - ID: " + order.getId());
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    // Ajouter un article à une commande
    @PostMapping("/{orderId}/items")
    public ResponseEntity<Order> addItemToOrder(
            @RequestParam Long userId,
            @PathVariable Long orderId,
            @RequestBody OrderItem orderItem) {
        Order updatedOrder = orderService.addItemToOrder(userId, orderId, orderItem);
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    // Récupérer une commande par ID
    @GetMapping("/{orderId}")
    @Operation(
            summary="Recuprer Liste des commandes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Succès",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Order.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Paramètre d'entrée non valide")
            }  )
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrder(orderId);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // Payer une commande
    @PutMapping("/{orderId}/pay")
    public ResponseEntity<Order> payForOrder(@PathVariable Long orderId) {
        Order paidOrder = orderService.payForOrder(orderId);
        if (paidOrder == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(paidOrder, HttpStatus.OK);
    }

    // Récupérer toutes les commandes pour un utilisateur
   /* @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    */

    // Endpoint pour récupérer toutes les commandes avec leurs items
    @GetMapping("")
    public ResponseEntity<List<OrderDto>> getAllOrdersWithItems() {
        // Ajout d'un message pour savoir quand la méthode est appelée
        System.out.println("Requête reçue pour récupérer toutes les commandes avec leurs items");

        List<OrderDto> orderDtos = orderService.getAllOrdersWithItems();

        // Afficher le nombre de commandes récupérées
        System.out.println("Nombre de commandes récupérées : " + orderDtos.size());

        // Afficher les détails des commandes récupérées (par exemple, le premier élément)
        if (!orderDtos.isEmpty()) {
            System.out.println("Détails de la première commande : ");
            OrderDto firstOrder = orderDtos.get(0);
            System.out.println("ID Commande : " + firstOrder.getId());
            System.out.println("User ID : " + firstOrder.getUserId());
            System.out.println("Total : " + firstOrder.getTotal());
            System.out.println("Status : " + firstOrder.getStatus());

            // Afficher les items de la première commande
            System.out.println("Items de la première commande : ");
            for (OrderItemDto item : firstOrder.getItems()) {
                System.out.println("ID Item : " + item.getId());
                System.out.println("Nom du produit : " + item.getProductName());
                System.out.println("Prix : " + item.getPrice());
                System.out.println("Quantité : " + item.getQuantity());
                System.out.println("Image Base64 : " + item.getImageBase64().substring(0, 50) + "...");  // Afficher une portion de l'image Base64
            }
        }

        return ResponseEntity.ok(orderDtos);
    }



}
