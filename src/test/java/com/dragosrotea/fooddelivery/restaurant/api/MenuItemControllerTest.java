package com.dragosrotea.fooddelivery.restaurant.api;

import com.dragosrotea.fooddelivery.common.api.GlobalExceptionHandler;

import com.dragosrotea.fooddelivery.restaurant.MenuItem;
import com.dragosrotea.fooddelivery.restaurant.MenuItemService;
import com.dragosrotea.fooddelivery.restaurant.Restaurant;
import com.dragosrotea.fooddelivery.restaurant.exception.RestaurantNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MenuItemControllerTest {

    @Mock
    private MenuItemService menuItemService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new MenuItemController(menuItemService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void returnsAvailableMenuItemsAsJson() throws Exception {
        MenuItem menuItem = menuItem();
        when(menuItemService.getAvailableMenu(1L)).thenReturn(List.of(menuItem));

        mockMvc.perform(get("/api/restaurants/1/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Margherita"))
                .andExpect(jsonPath("$[0].price").value(32.00))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    void createsMenuItemFromValidJson() throws Exception {
        MenuItem menuItem = menuItem();
        when(menuItemService.addMenuItem(
                1L,
                "Margherita",
                "Tomato, mozzarella and basil",
                new BigDecimal("32.00"),
                "Pizza"
        )).thenReturn(menuItem);

        mockMvc.perform(post("/api/restaurants/1/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Margherita",
                                  "description": "Tomato, mozzarella and basil",
                                  "price": 32.00,
                                  "category": "Pizza"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Margherita"))
                .andExpect(jsonPath("$.category").value("Pizza"));

        verify(menuItemService).addMenuItem(
                1L,
                "Margherita",
                "Tomato, mozzarella and basil",
                new BigDecimal("32.00"),
                "Pizza"
        );
    }

    @Test
    void rejectsNegativeMenuItemPrice() throws Exception {
        mockMvc.perform(post("/api/restaurants/1/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Margherita",
                                  "description": "Tomato, mozzarella and basil",
                                  "price": -0.01,
                                  "category": "Pizza"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.price")
                        .value("Price must be zero or greater"));

        verifyNoInteractions(menuItemService);
    }

    @Test
    void returnsNotFoundWhenRestaurantDoesNotExist() throws Exception {
        when(menuItemService.getAvailableMenu(99L))
                .thenThrow(new RestaurantNotFoundException(99L));

        mockMvc.perform(get("/api/restaurants/99/menu-items"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.path")
                        .value("/api/restaurants/99/menu-items"));
    }

    private MenuItem menuItem() {
        Restaurant restaurant = new Restaurant(
                "Urban Pizza",
                "10 Main Street",
                "Bucharest"
        );

        return new MenuItem(
                restaurant,
                "Margherita",
                "Tomato, mozzarella and basil",
                new BigDecimal("32.00"),
                "Pizza"
        );
    }
}
