package org.example.menuservice.service;

import org.example.menuservice.Repository.MenuRepository;
import org.example.menuservice.dto.MenuDto;
import org.example.menuservice.entite.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.awt.SystemColor.menu;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    private Menu existingMenu;
    private Menu updatedMenu;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup an existing menu for testing
        existingMenu = new Menu();
        existingMenu.setId(1L);
        existingMenu.setName("Pizza");
        existingMenu.setDescription("Delicious cheese pizza");
        existingMenu.setCategory("Main");
        existingMenu.setPrice(12.0);
        existingMenu.setImage("image1.png".getBytes());
        existingMenu.setPromotion(false);

        // Setup an updated menu
        updatedMenu = new Menu();
        updatedMenu.setName("Updated Pizza");
        updatedMenu.setDescription("Delicious cheese pizza with more cheese");
        updatedMenu.setCategory("Main");
        updatedMenu.setPrice(14.0);
        updatedMenu.setImage("updated_image.png".getBytes());
        updatedMenu.setPromotion(true);
    }
    @Test
    void getAllMenus() {
        // Mocking data
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

        // Mocking repository call
        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu1, menu2));

        // Call the service method
        List<MenuDto> result = menuService.getAllMenus();

        // Assertions on the first MenuDto object
        MenuDto menuDto1 = result.get(0);
        assertEquals("Pizza", menuDto1.getName());
        assertEquals("Main", menuDto1.getCategory());
        assertEquals("Delicious cheese pizza", menuDto1.getDescription());
        assertEquals(12.0, menuDto1.getPrice());
        assertEquals(false, menuDto1.getPromotion());

        // Assertions on the second MenuDto object
        MenuDto menuDto2 = result.get(1);
        assertEquals("Burger", menuDto2.getName());
        assertEquals("Main", menuDto2.getCategory());
        assertEquals("Tasty beef burger", menuDto2.getDescription());
        assertEquals(10.0, menuDto2.getPrice());
        assertEquals(true, menuDto2.getPromotion());

        // Verify repository interaction
        verify(menuRepository, times(1)).findAll();
    }


    @Test
    void getMenusByCategory() {
        // Mocking data
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

        // Mocking the menuRepository call
        when(menuRepository.findByCategory("Main")).thenReturn(Arrays.asList(menu1, menu2));

        // Call the service method
        List<Menu> result = menuService.getMenusByCategory("Main");

        // Assertions
        assertNotNull(result);
        assertEquals(2, result.size()); // Should return 2 menus
        assertEquals("Pizza", result.get(0).getName());
        assertEquals("Burger", result.get(1).getName());

        // Verify repository interaction
        verify(menuRepository, times(1)).findByCategory("Main");
    }

    @Test
    void getMenuById() {
        // Mocking data for the menu with id 1
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Pizza");
        menu.setDescription("Delicious cheese pizza");
        menu.setCategory("Main");
        menu.setPrice(12.0);
        menu.setImage("image1.png".getBytes());
        menu.setPromotion(false);

        // Mocking the repository call for finding the menu by ID
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        // Call the service method
        Optional<Menu> result = menuService.getMenuById(1L);

        // Assertions
        assertTrue(result.isPresent()); // The result should be present
        assertEquals("Pizza", result.get().getName());
        assertEquals("Main", result.get().getCategory());
        assertEquals(12.0, result.get().getPrice());
        assertEquals(false, result.get().getPromotion());

        // Verify repository interaction
        verify(menuRepository, times(1)).findById(1L);
    }

    @Test
    void addMenu() {
        // Mocking a Menu object
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Pizza");
        menu.setDescription("Delicious cheese pizza");
        menu.setCategory("Main");
        menu.setPrice(12.0);
        menu.setImage("image1.png".getBytes());
        menu.setPromotion(false);

        // Mocking the repository to return the same menu when saved
        when(menuRepository.save(menu)).thenReturn(menu);

        // Call the service method
        Menu result = menuService.addMenu(menu);

        // Assertions
        assertNotNull(result); // Ensure the result is not null
        assertEquals(menu.getId(), result.getId());
        assertEquals(menu.getName(), result.getName());
        assertEquals(menu.getDescription(), result.getDescription());
        assertEquals(menu.getCategory(), result.getCategory());
        assertEquals(menu.getPrice(), result.getPrice());
        assertEquals(menu.getImage(), result.getImage());
        assertEquals(menu.getPromotion(), result.getPromotion());

        // Verify repository interaction
        verify(menuRepository, times(1)).save(menu);
    }

    @Test
    void updateMenu() {
        // Mocking the repository to return the existing menu
        when(menuRepository.findById(1L)).thenReturn(Optional.of(existingMenu));
        when(menuRepository.save(existingMenu)).thenReturn(existingMenu);

        // Call the service method
        Menu result = menuService.updateMenu(1L, updatedMenu);

        // Assertions to verify that the menu has been updated correctly
        assertNotNull(result);
        assertEquals("Updated Pizza", result.getName());
        assertEquals("Delicious cheese pizza with more cheese", result.getDescription());
        assertEquals("Main", result.getCategory());
        assertEquals(14.0, result.getPrice());
        assertArrayEquals(updatedMenu.getImage(), result.getImage());
        assertTrue(result.getPromotion());

        // Verify repository interaction
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, times(1)).save(existingMenu);
    }

    @Test
    void updateMenu_MenuNotFound() {
        // Mocking the repository to return an empty Optional, simulating that the menu was not found
        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method and assert that it throws the expected exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuService.updateMenu(1L, updatedMenu);
        });

        assertEquals("Menu not found", exception.getMessage());

        // Verify repository interaction
        verify(menuRepository, times(1)).findById(1L);
        verify(menuRepository, times(0)).save(any(Menu.class));
    }

   /* @Test
    void deleteMenu() {
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        // Call the service method (which will call deleteById internally)
        menuService.deleteMenu(1L);

        // Verify that the repository's deleteById method was called with the correct ID
        verify(menuRepository, times(1)).deleteById(1L);
    }*/
}