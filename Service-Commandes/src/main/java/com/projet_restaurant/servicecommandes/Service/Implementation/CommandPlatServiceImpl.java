package com.projet_restaurant.servicecommandes.Service.Implementation;

import com.projet_restaurant.servicecommandes.Client.MenuRestFeign;
import com.projet_restaurant.servicecommandes.Entity.CommandPlat;
import com.projet_restaurant.servicecommandes.Entity.Commande;
import com.projet_restaurant.servicecommandes.Repository.CommandPlatRepository;
import com.projet_restaurant.servicecommandes.Repository.CommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class CommandPlatServiceImpl {
    @Autowired
    private MenuRestFeign menuRestFeign;
    @Autowired
    private CommandPlatRepository commandPlatRepository;
    @Autowired
    private CommandRepository commandRepository;
    public CommandPlatServiceImpl(MenuRestFeign menuRestFeign) {
        this.menuRestFeign = menuRestFeign;
    }
    public Map<String, Object> executeGraphQLQuery(String query, Map<String, Object> variables) {
        Map<String, Object> request = new HashMap<>();
        request.put("query", query);
        if (variables != null) {
            request.put("variables", variables);
        }
        Map<String, Object> response = menuRestFeign.executeGraphQLQuery(request);
        System.out.println("GraphQL Response: " + response); // Ajoutez ce log pour vérifier la réponse
        return response;
    }

    public List<?> getAllMenus() {
        String query = "{ menus { id name description price category } }";
        Map<String, Object> response = executeGraphQLQuery(query, null);

        // Extraire les données
        Map<String, Object> data = (Map<String, Object>) response.get("data"); // Récupère l'objet "data"
        if (data != null) {
            return (List<?>) data.get("menus"); // Accède à "menus" dans "data"
        }
        // Retourne une liste vide si aucune donnée n'est trouvée
        return List.of();
    }

    public Map<String, Object> getMenuById(Long id) {
        System.out.println("Fetching menu with ID: " + id);

        // Construire la requête GraphQL
        String query = "query($id: ID!) { menuById(id: $id) { id name description price category } }";
        Map<String, Object> variables = Map.of("id", id);
        Map<String, Object> response = executeGraphQLQuery(query, variables);

        // Afficher la réponse brute
        System.out.println("Raw GraphQL Response: " + response);

        // Vérifier si "data" est présent
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        if (data == null) {
            System.out.println("GraphQL response does not contain 'data'.");
            return null;
        }

        // Vérifier si "menuById" est présent
        Map<String, Object> menuById = (Map<String, Object>) data.get("menuById");
        if (menuById == null) {
            System.out.println("Menu with ID: " + id + " not found in GraphQL response.");
            return null;
        }

        // Menu trouvé
        System.out.println("Menu fetched successfully: " + menuById);
        return menuById;
    }


    public List<?> getMenusByCategory(String category) {
        String query = "query($category: String!) { menuByCategory(category: $category) { id name description price } }";
        Map<String, Object> variables = Map.of("category", category);
        Map<String, Object> response = executeGraphQLQuery(query, variables);
        return (List<?>) response.get("data.menuByCategory");
    }

    // Ajouter un plat à une commande
    public void addPlatToCommande(Long commandeId, Long menuId, int quantite) {
        System.out.println("Début de la méthode addPlatToCommande avec commandeId : " + commandeId + ", menuId : " + menuId + ", quantite : " + quantite);

        // Recherche de la commande
        Commande commande = commandRepository.findById(commandeId)
                .orElseThrow(() -> new NoSuchElementException("Commande introuvable avec l'ID : " + commandeId));
        System.out.println("Commande trouvée avec ID : " + commandeId);

        // Recherche du plat via l'ID menu
        Map<String, Object> menuData = getMenuById(menuId);

        if (menuData != null) {
            // Crée une nouvelle instance de CommandPlat
            CommandPlat commandePlat = new CommandPlat();
            commandePlat.setMenuId(menuId);
            commandePlat.setQuantite(quantite);
            commandePlat.setPrix((Double) menuData.get("price"));
            System.out.println("Création de CommandPlat avec menuId : " + menuId + ", prix : " + menuData.get("price"));

            // Ajoute le plat à la commande
            commande.addPlat(commandePlat);

            // Enregistre la commande avec les nouvelles relations
            commandRepository.save(commande);
            System.out.println("Plat ajouté à la commande ID : " + commandeId);
        } else {
            // Gestion du cas où le plat n'est pas trouvé
            System.err.println("Menu introuvable avec l'ID : " + menuId);
            throw new NoSuchElementException("Menu introuvable avec l'ID : " + menuId);
        }
    }

    public void updateCommandeStatut(Long commandeId, String statut) {
        System.out.println("Début de la méthode updateCommandeStatut avec commandeId : " + commandeId + " et statut : " + statut);

        // Recherche de la commande par son ID
        Commande commande = commandRepository.findById(commandeId)
                .orElseThrow(() -> new NoSuchElementException("Commande not found with ID: " + commandeId));
        System.out.println("Commande trouvée avec ID : " + commandeId);

        // Met à jour le statut de la commande
        commande.setStatut(statut);
        commandRepository.save(commande);
        System.out.println("Statut de la commande ID : " + commandeId + " mis à jour à : " + statut);
    }

    public Double calculateTotalForCommande(Long commandeId) {
        System.out.println("Début de la méthode calculateTotalForCommande avec commandeId : " + commandeId);

        // Récupère tous les plats de la commande
        List<CommandPlat> commandePlats = commandPlatRepository.findByCommandeId(commandeId);
        System.out.println("Récupération des plats pour la commande ID : " + commandeId);

        // Calcule le total
        Double total = commandePlats.stream()
                .mapToDouble(plat -> plat.getQuantite() * plat.getPrix())
                .sum();
        System.out.println("Total calculé pour la commande ID : " + commandeId + " est : " + total);

        return total;
    }

    public void finalizeCommande(Long commandeId) {
        System.out.println("Début de la méthode finalizeCommande avec commandeId : " + commandeId);

        updateCommandeStatut(commandeId, "en cours");

        // Optionnel : envoyer une notification de confirmation ou un e-mail à l'utilisateur
        // par exemple, envoyerNotificationCommande(commandeId);
        System.out.println("Commande ID : " + commandeId + " finalisée.");
    }


}
