package org.example.menuservice.service;

import org.example.menuservice.Repository.MenuRepository;
import org.example.menuservice.entite.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
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
