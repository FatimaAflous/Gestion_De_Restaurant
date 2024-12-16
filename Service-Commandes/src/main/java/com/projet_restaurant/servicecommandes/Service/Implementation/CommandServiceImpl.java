package com.projet_restaurant.servicecommandes.Service.Implementation;

import com.projet_restaurant.servicecommandes.Client.UserRestFeign;
import com.projet_restaurant.servicecommandes.Dto.UserDto;
import com.projet_restaurant.servicecommandes.Entity.CommandPlat;
import com.projet_restaurant.servicecommandes.Entity.Commande;
import com.projet_restaurant.servicecommandes.Repository.CommandRepository;
import com.projet_restaurant.servicecommandes.Service.MenuQuantite;
import jakarta.persistence.Tuple;
import org.hibernate.type.descriptor.java.ObjectJavaType;
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
    private CommandRepository commandRepository;
    @Autowired
    private final UserRestFeign userRestFeign;
    @Autowired
    private CommandPlatServiceImpl commandPlatService;


    @Autowired
    public CommandServiceImpl(UserRestFeign userRestFeign) {
        this.userRestFeign = userRestFeign;
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
// Creer une Commande

  /*  public Long createCommande(Long userId, List<Long> menuIds, String token) {
        System.out.println("Paramètres d'entrée : userId=" + userId + ", menuIds=" + menuIds + ", token=" + token);

        try {
            // Étape 1 : Vérification des paramètres d'entrée
            if (userId == null) {
                System.err.println("Erreur : userId est null.");
                throw new IllegalArgumentException("userId ne peut pas être null.");
            }
            if (menuIds == null || menuIds.isEmpty()) {
                System.err.println("Erreur : menuIds est null ou vide.");
                throw new IllegalArgumentException("menuIds ne peut pas être null ou vide.");
            }

            if (token == null || token.isBlank()) {
                System.err.println("Erreur : token est null ou vide.");
                throw new IllegalArgumentException("Le token est requis.");
            }

            // Récupère les informations de l'utilisateur
            Object userDto = getUserById(userId, token);
            if (userDto == null) {
                System.err.println("Utilisateur non trouvé avec l'ID : " + userId);
                throw new NoSuchElementException("Utilisateur non trouvé avec l'ID : " + userId);
            }
            System.out.println("Utilisateur trouvé avec succès : " + userDto);

            // Crée la commande avec les plats sélectionnés
            Commande commande = new Commande();
            commande.setClientId(userId);
            commande.setDate(LocalDateTime.now());
            commande.setStatut("en cours");
            commande.setTotal(0.0); // Initialisé à 0, sera mis à jour après ajout des plats

            // Ajout des plats à la commande avant de la persister
            for (Long menuId : menuIds) {
                Map<String, Object> menuData = commandPlatService.getMenuById(menuId);
                if (menuData == null) {
                    System.err.println("Menu introuvable avec l'ID : " + menuId);
                    throw new NoSuchElementException("Menu introuvable avec l'ID : " + menuId);
                }

                // Crée une nouvelle instance de CommandPlat
                CommandPlat commandePlat = new CommandPlat();
                commandePlat.setMenuId(menuId);
                commandePlat.setQuantite(1); // Quantité de 1 par défaut
                commandePlat.setPrix((Double) menuData.get("price"));
                commande.addPlat(commandePlat); // Ajoute le plat à la commande
            }

            commande = commandRepository.save(commande); // Persiste la commande
            System.out.println("Commande créée avec ID : " + commande.getId());

            // Calcule le total de la commande
            Double total = commandPlatService.calculateTotalForCommande(commande.getId());
            commande.setTotal(total);
            commandRepository.save(commande); // Mise à jour de la commande avec le total
            System.out.println("Total de la commande ID : " + commande.getId() + " mis à jour à : " + total);

            // Finalise la commande (en statut "terminé")
            commandPlatService.finalizeCommande(commande.getId());
            System.out.println("Commande ID : " + commande.getId() + " a été finalisée.");

            return commande.getId(); // Retourne l'ID de la commande créée
        } catch (Exception e) {
            // Étape de gestion des erreurs
            System.err.println("Erreur dans createCommande : " + e.getMessage());
            e.printStackTrace();
            throw e; // Relancer l'exception pour le gestionnaire d'erreurs global
        }
    }

*/
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

          return commande.getId();
      } catch (Exception e) {
          System.err.println("Erreur dans createCommande : " + e.getMessage());
          throw e;
      }
  }


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

}
