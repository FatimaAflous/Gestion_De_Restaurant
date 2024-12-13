package com.projet_restaurant.servicecommandes.Web;

import com.projet_restaurant.servicecommandes.Dto.UserDto;
import com.projet_restaurant.servicecommandes.Entity.Commande;
import com.projet_restaurant.servicecommandes.Service.Implementation.CommandServiceImpl;
import com.projet_restaurant.servicecommandes.Service.MenuQuantite;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/commande")
public class CommandAPI {
    private final CommandServiceImpl commandService;

    @Autowired
    public CommandAPI(CommandServiceImpl commandService) {
        this.commandService = commandService;
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserForCommande(@PathVariable Long id, HttpServletRequest request) {
        try {
            // Récupérer le token de l'entête HTTP Authorization
            String token = request.getHeader("Authorization").substring("Bearer ".length());

            // Récupération de l'utilisateur par ID
            Object userDto = commandService.getUserById(id, token);

            // Vérification de l'existence de l'utilisateur
            if (userDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Utilisateur non trouvé"));
            }

            // Retourner l'utilisateur récupéré
            return ResponseEntity.ok(userDto);

        } catch (RuntimeException ex) {
            // Gérer l'exception et retourner une réponse 404
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur interne"));
        }
    }
    // Créer une commande
    @PostMapping("/create_commande")
    public ResponseEntity<Long> createCommande(
            @RequestParam Long userId,
            @RequestBody List<MenuQuantite> menuQuantities,
            @RequestHeader("Authorization") String token) {
        System.out.println("Paramètres d'entrée : userId=" + userId + ", menuQuantities=" + menuQuantities + ", token=" + token);

        try {
            Long commandeId = commandService.createCommande(userId, menuQuantities, token);
            System.out.println("Commande créée avec succès. ID : " + commandeId);

            return ResponseEntity.status(HttpStatus.CREATED).body(commandeId);
        } catch (NoSuchElementException e) {
            System.err.println("Utilisateur non trouvé avec l'ID : " + userId);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            System.err.println("Erreur inattendue lors de la création de la commande pour l'utilisateur ID : " + userId + " avec menuQuantities : " + menuQuantities);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } finally {
            System.out.println("Fin de la méthode createCommande pour userId : " + userId);
        }
    }


    @GetMapping("/userCommand/{userId}")
    public ResponseEntity<List<Commande>> getCommandesByUserId(@PathVariable Long userId) {
        List<Commande> commandes = commandService.getCommandesByUserId(userId);
        if (commandes.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content si aucune commande trouvée
        }
        return ResponseEntity.ok(commandes); // 200 OK avec la liste des commandes
    }
    @GetMapping("/All")
    public ResponseEntity<List<Commande>> getAllCommands() {
        List<Commande> commandes = commandService.getAllCommandes();
        if (commandes.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content si aucune commande trouvée
        }
        return ResponseEntity.ok(commandes); // 200 OK avec la liste des commandes
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> annulerCommande(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            commandService.annulerCommande(id, token);
            return ResponseEntity.ok("Commande annulée avec succès.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'annulation de la commande.");
        }
    }

}
