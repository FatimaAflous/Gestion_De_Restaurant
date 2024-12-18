package com.projet_restaurant.servicecommandes.Service.Implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.projet_restaurant.servicecommandes.Client.UserRestFeign;
import com.projet_restaurant.servicecommandes.Dto.PaymentRequest;
import com.projet_restaurant.servicecommandes.Dto.UserDto;
import com.projet_restaurant.servicecommandes.Entity.CommandPlat;
import com.projet_restaurant.servicecommandes.Entity.Commande;
import com.projet_restaurant.servicecommandes.Repository.CommandRepository;
import com.projet_restaurant.servicecommandes.Service.MenuQuantite;
import jakarta.persistence.Tuple;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CommandServiceImpl {
    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private CommandRepository commandRepository;
    @Autowired
    private final UserRestFeign userRestFeign;
    @Autowired
    private CommandPlatServiceImpl commandPlatService;



    @Autowired
    public CommandServiceImpl(UserRestFeign userRestFeign , RabbitTemplate rabbitTemplate) {
        this.userRestFeign = userRestFeign;
        this.rabbitTemplate = rabbitTemplate;
    }
//Recuperer les infos des cleints a travers srvice User via Feign
    public Object getUserById(Long userId, String token) {
        System.out.println("Attempting to get user with ID: " + userId + " using token: " + token);
        // Construction de l'en-tête Authorization
      //  String bearerToken = "Bearer " + token;
        // Appel Feign pour récupérer l'utilisateur
        ResponseEntity<Object> response = userRestFeign.getUserById(token, userId);
        System.out.println("Status Code from Feign response: " + response.getStatusCodeValue());
        Object userDto = response.getBody();
        if (userDto == null) {
            System.out.println("Error: Received null UserDto");
        } else {
            System.out.println("Successfully retrieved UserDto for user ID " + userId);
        }
        return userDto;
    }

  public Long createCommande(Long userId, List<MenuQuantite> menus, String token) {
      System.out.println("Paramètres d'entrée : userId=" + userId + ", menus=" + menus + ", token=" + token);

      try {
          // Vérifications des paramètres
          if (userId == null) {
              throw new IllegalArgumentException("userId ne peut pas être null.");
          }
          if (menus == null || menus.isEmpty()) {
              throw new IllegalArgumentException("La liste des menus ne peut pas être vide.");
          }
          if (token == null || token.isBlank()) {
              throw new IllegalArgumentException("Le token est requis.");
          }

          // Vérifie et récupère l'utilisateur
          Object userDto = getUserById(userId, token);
          if (userDto == null) {
              throw new NoSuchElementException("Utilisateur non trouvé avec l'ID : " + userId);
          }

          // Crée une nouvelle commande
          Commande commande = new Commande();
          commande.setClientId(userId);
          commande.setDate(LocalDateTime.now());
          commande.setStatut("en cours");
          commande.setTotal(0.0);

          // Ajoute chaque plat à la commande
          for (MenuQuantite menuQuantite : menus) {
              Map<String, Object> menuData = commandPlatService.getMenuById(menuQuantite.getMenuId());
              if (menuData == null) {
                  throw new NoSuchElementException("Menu introuvable avec l'ID : " + menuQuantite.getMenuId());
              }

              // Ajoute le plat avec la quantité spécifiée
              CommandPlat commandePlat = new CommandPlat();
              commandePlat.setMenuId(menuQuantite.getMenuId());
              commandePlat.setQuantite(menuQuantite.getQuantite());
              commandePlat.setPrix((Double) menuData.get("price") * menuQuantite.getQuantite()); // Total pour ce plat
              commande.addPlat(commandePlat);
          }

          // Enregistre la commande
          commande = commandRepository.save(commande);

          // Calcule et met à jour le total
          Double total = commandPlatService.calculateTotalForCommande(commande.getId());
          commande.setTotal(total);
          commandRepository.save(commande);

          // Finalise la commande
          commandPlatService.finalizeCommande(commande.getId());
          // Publication de la commande dans RabbitMQ
          envoyerCommande(commande);
          return commande.getId();
      } catch (Exception e) {
          System.err.println("Erreur dans createCommande : " + e.getMessage());
          throw e;
      }
  }
  //méthode pour transférer les informations de commande au service de paiement



    //liste des commandes par client
    public List<Commande> getCommandesByUserId(Long userId) {
        return commandRepository.findByClientId(userId);
    }
    public List<Commande> getAllCommandes() {
        return commandRepository.findAllByOrderByIdDesc();
    }
  /*

    public Commande mettreAJourStatut(Long id, String nouveauStatut) {
        Commande commande = commandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
        commande.setStatut(nouveauStatut);
        return commandRepository.save(commande);
    }
     */
  public void annulerCommande(Long commandeId, String token) {
      System.out.println("Paramètres d'entrée : commandeId=" + commandeId + ", token=" + token);

      try {
          // Vérifications des paramètres
          if (commandeId == null) {
              throw new IllegalArgumentException("commandeId ne peut pas être null.");
          }
          if (token == null || token.isBlank()) {
              throw new IllegalArgumentException("Le token est requis.");
          }

          // Vérifie si la commande existe
          Optional<Commande> optionalCommande = commandRepository.findById(commandeId);
          if (optionalCommande.isEmpty()) {
              throw new NoSuchElementException("Commande introuvable avec l'ID : " + commandeId);
          }

          // Récupère la commande
          Commande commande = optionalCommande.get();

          // Suppression des plats associés
          List<CommandPlat> plats = commande.getPlats();
          if (plats != null && !plats.isEmpty()) {
              plats.forEach(plat -> commandPlatService.deletePlatById(plat.getId()));
          }

          // Suppression de la commande
          commandRepository.delete(commande);
          System.out.println("Commande avec l'ID " + commandeId + " a été annulée et supprimée.");
      } catch (Exception e) {
          System.err.println("Erreur dans annulerCommande : " + e.getMessage());
          throw e;
      }
  }
//Gestion de paiement
// Méthode pour envoyer les informations de commande à RabbitMQ
public void envoyerCommande(Commande commande) {
    System.out.println("Envoi de la commande à RabbitMQ : " + commande);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());  // Ajouter le module pour gérer LocalDateTime

    try {
        String commandeJson = objectMapper.writeValueAsString(commande);
        rabbitTemplate.convertAndSend("commande.queue", commandeJson);
        System.out.println("Commande envoyée avec succès.");
    } catch (JsonProcessingException e) {
        System.err.println("Erreur de conversion de la commande en JSON : " + e.getMessage());
        throw new RuntimeException("Erreur lors de la conversion de la commande en JSON", e);
    }
}
}



