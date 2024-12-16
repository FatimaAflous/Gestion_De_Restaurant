package org.example.menuservice.controller;

import org.example.menuservice.entite.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
/*import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
*/
import org.springframework.stereotype.Controller;
import org.example.menuservice.service.MenuService;

import java.util.List;

@Controller
public class MenuGraphQLController {
    @Autowired
    private MenuService menuService;

    // Requête pour obtenir tous les menus

    @QueryMapping
    public List<Menu> menus() {
        return menuService.getAllMenus();
    }
    // Requête pour obtenir les menus par catégorie
    @QueryMapping
    public List<Menu> menuByCategory(@Argument String category) {
        return menuService.getMenusByCategory(category);
    }

    // Requête pour obtenir un menu par ID
    @QueryMapping
    public Menu menuById(@Argument Long id) {
        return menuService.getMenuById(id).orElseThrow(() -> new RuntimeException("Menu not found"));
    }

    // Mutation pour ajouter un menu
    @MutationMapping
   // @PreAuthorize("hasAuthority('ADMIN')")
    public Menu addMenu(
            @Argument String name,
            @Argument String description,
            @Argument String category,
            @Argument Double price,
            @Argument String image, // Ajout de l'image
            @Argument Boolean isPromotion,
            @Argument String allergens

    ) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setDescription(description);
        menu.setCategory(category);
        menu.setPrice(price);
        menu.setImage(image); // Affecter l'image
        menu.setPromotion(isPromotion);

        return menuService.addMenu(menu);
    }

    // Mutation pour mettre à jour un menu
    @MutationMapping
    //@PreAuthorize("hasAuthority('ADMIN')")
    public Menu updateMenu(
            @Argument Long id,
            @Argument String name,
            @Argument String description,
            @Argument String category,
            @Argument Double price,
            @Argument String image, // Ajout de l'image
            @Argument Boolean isPromotion,
            @Argument String allergens
    ) {
        Menu updatedMenu = new Menu();
        updatedMenu.setName(name);
        updatedMenu.setDescription(description);
        updatedMenu.setCategory(category);
        updatedMenu.setPrice(price);
        updatedMenu.setImage(image); // Affecter l'image
        updatedMenu.setPromotion(isPromotion);
        return menuService.updateMenu(id, updatedMenu);
    }

    // Mutation pour supprimer un menu
    @MutationMapping
    //@PreAuthorize("hasAuthority('ADMIN')")
    public String deleteMenu(@Argument Long id) {
        menuService.deleteMenu(id);
        return "Menu deleted successfully";
    }
}
