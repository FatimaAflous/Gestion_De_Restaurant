package org.example.menuservice.Repository;

import org.example.menuservice.entite.Menu;
import org.example.menuservice.service.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuRepositoryTest {
    @Mock
    private MenuRepository menuRepository; // Mock du repository

    @InjectMocks
    private MenuService menuService; // Le service qui utilise le repository

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialisation des mocks
    }
    @Test
    void findByCategory() {
        // Données fictives pour tester
        Menu menu1 = new Menu();
        menu1.setId(1L);
        menu1.setName("Pizza");
        menu1.setDescription("Delicious cheese pizza");
        menu1.setCategory("Main");
        menu1.setPrice(12.0);
        menu1.setImage("image1.png".getBytes());
        menu1.setPromotion(false);

        Menu menu2 = new Menu();
        menu2.setId(2L);
        menu2.setName("Burger");
        menu2.setDescription("Tasty beef burger");
        menu2.setCategory("Main");
        menu2.setPrice(10.0);
        menu2.setImage("image2.png".getBytes());
        menu2.setPromotion(true);

        // Simulation du comportement du repository
        when(menuRepository.findByCategory("Main")).thenReturn(Arrays.asList(menu1, menu2));

        // Appel de la méthode du service
        List<Menu> result = menuService.getMenusByCategory("Main");

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Pizza", result.get(0).getName());
        assertEquals("Burger", result.get(1).getName());

        // Vérification que la méthode du repository a bien été appelée
        verify(menuRepository, times(1)).findByCategory("Main");

    }
}