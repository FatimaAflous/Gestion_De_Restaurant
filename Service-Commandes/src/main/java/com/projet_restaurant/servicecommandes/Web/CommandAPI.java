package com.projet_restaurant.servicecommandes.Web;

import com.projet_restaurant.servicecommandes.Dto.UserDto;
import com.projet_restaurant.servicecommandes.Service.Implementation.CommandServiceImpl;
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
            @RequestBody List<Long> menuIds,
            @RequestHeader("Authorization") String token) {
        System.out.println("---- Début de createCommande ----");
        System.out.println("Paramètres d'entrée : userId=" + userId + ", menuIds=" + menuIds + ", token=" + token);

        try {
            // Appel à la méthode service pour créer la commande
            Long commandeId = commandService.createCommande(userId, menuIds, token);
            System.out.println("Commande créée avec succès. ID : " + commandeId);

            return ResponseEntity.status(HttpStatus.CREATED).body(commandeId);
        } catch (NoSuchElementException e) {
            // Gestion spécifique des utilisateurs non trouvés
            System.err.println("Utilisateur non trouvé avec l'ID : " + userId);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Gestion des autres erreurs inattendues
            System.err.println("Erreur inattendue lors de la création de la commande pour l'utilisateur ID : " + userId + " avec menuIds : " + menuIds);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } finally {
            System.out.println("Fin de la méthode createCommande pour userId : " + userId);
        }
    }




}
