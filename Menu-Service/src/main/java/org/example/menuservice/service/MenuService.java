package org.example.menuservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.example.menuservice.Repository.MenuRepository;
import org.example.menuservice.dto.MenuDto;
import org.example.menuservice.entite.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;


    @CircuitBreaker(name = "menuService", fallbackMethod = "fallbackMenuService")
    @Retry(name = "menuService", fallbackMethod = "fallbackMenuService")
    public List<MenuDto> getAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream().map(MenuDto::new).collect(Collectors.toList());
    }
    public List<MenuDto> fallbackMenuService(Throwable t) {
        // Log l'erreur dans la console ou un système de log
        System.out.println("Erreur lors de l'appel au service Menu : " + t.getMessage());
        // Retourner une liste vide ou d'autres données par défaut
        return new ArrayList<>();  // Liste vide, mais tu pourrais aussi retourner un message d'erreur comme une Exception
    }


    public List<Menu> getMenusByCategory(String category) {
        return menuRepository.findByCategory(category);
    }

    public Optional<Menu> getMenuById(Long id) {
        return menuRepository.findById(id);
    }
    public Menu addMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public Menu updateMenu(Long id, Menu updatedMenu) {
        return menuRepository.findById(id).map(menu -> {
            menu.setName(updatedMenu.getName());
            menu.setDescription(updatedMenu.getDescription());
            menu.setCategory(updatedMenu.getCategory());
            menu.setPrice(updatedMenu.getPrice());
            menu.setImage(updatedMenu.getImage()); // Mettre à jour l'image
            menu.setPromotion(updatedMenu.getPromotion());
            return menuRepository.save(menu);
        }).orElseThrow(() -> new RuntimeException("Menu not found"));
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }
}
