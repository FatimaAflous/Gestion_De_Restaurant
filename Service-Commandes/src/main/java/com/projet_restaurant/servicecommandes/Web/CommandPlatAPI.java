package com.projet_restaurant.servicecommandes.Web;

import com.projet_restaurant.servicecommandes.Service.Implementation.CommandPlatServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/commande/menu")
public class CommandPlatAPI {
    @Autowired
    private CommandPlatServiceImpl commandPlatService;

    // Endpoint pour obtenir tous les menus
    @GetMapping
    public ResponseEntity<List<?>> getAllMenus() {
        List<?> menus = commandPlatService.getAllMenus();
        return ResponseEntity.ok(menus);
    }

    // Endpoint pour obtenir un menu par ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMenuById(@PathVariable Long id) {
        System.out.println("Received request to fetch menu with ID: " + id);
        Map<String, Object> menu;
        try {
            menu = commandPlatService.getMenuById(id);
            if (menu == null) {
                System.out.println("Menu with ID: " + id + " not found.");
                return ResponseEntity.notFound().build();
            }
            System.out.println("Menu with ID: " + id + " found successfully.");
            return ResponseEntity.ok(menu);
        } catch (Exception e) {
            System.out.println("Error fetching menu with ID: " + id + " - " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Unable to fetch menu. Please try again later."));
        }
    }
    // Endpoint pour obtenir des menus par catégorie
    @GetMapping("/category/{category}")
    public ResponseEntity<List<?>> getMenusByCategory(@PathVariable String category) {
        List<?> menus = commandPlatService.getMenusByCategory(category);
        return ResponseEntity.ok(menus);
    }

    // Ajouter un plat à une commande
    @PostMapping("/{commandeId}/plats")
    public ResponseEntity<String> addPlatToCommande(
            @PathVariable Long commandeId,
            @RequestParam Long menuId,
            @RequestParam int quantite) {
        try {
            commandPlatService.addPlatToCommande(commandeId, menuId, quantite);
            return ResponseEntity.ok("Plat ajouté à la commande avec succès.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Mettre à jour le statut d'une commande
    @PutMapping("/{commandeId}/statut")
    public ResponseEntity<String> updateCommandeStatut(
            @PathVariable Long commandeId,
            @RequestParam String statut) {
        try {
            commandPlatService.updateCommandeStatut(commandeId, statut);
            return ResponseEntity.ok("Statut de la commande mis à jour avec succès.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Calculer le total d'une commande
    @GetMapping("/{commandeId}/total")
    public ResponseEntity<Double> calculateTotalForCommande(@PathVariable Long commandeId) {
        try {
            Double total = commandPlatService.calculateTotalForCommande(commandeId);
            return ResponseEntity.ok(total);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Finaliser une commande
    @PostMapping("/{commandeId}/finalize")
    public ResponseEntity<String> finalizeCommande(@PathVariable Long commandeId) {
        try {
            commandPlatService.finalizeCommande(commandeId);
            return ResponseEntity.ok("Commande finalisée avec succès.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
