package com.dragosrotea.fooddelivery.restaurant;

import com.dragosrotea.fooddelivery.restaurant.exception.DuplicateMenuItemException;
import com.dragosrotea.fooddelivery.restaurant.exception.InvalidMenuItemPriceException;
import com.dragosrotea.fooddelivery.restaurant.exception.RestaurantNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuItemService menuItemService;

    @Test
    void returnsOnlyAvailableMenuItemsForActiveRestaurant() {
        Restaurant restaurant = restaurant();
        MenuItem menuItem = menuItem(restaurant);
        when(restaurantRepository.findByIdAndActiveTrue(1L))
                .thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findByRestaurantIdAndAvailableTrue(1L))
                .thenReturn(List.of(menuItem));

        List<MenuItem> result = menuItemService.getAvailableMenu(1L);

        assertEquals(1, result.size());
        assertSame(menuItem, result.get(0));
    }

    @Test
    void hidesMenuWhenRestaurantIsMissingOrInactive() {
        when(restaurantRepository.findByIdAndActiveTrue(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                RestaurantNotFoundException.class,
                () -> menuItemService.getAvailableMenu(1L)
        );
        verify(menuItemRepository, never())
                .findByRestaurantIdAndAvailableTrue(1L);
    }

    @Test
    void returnsAllMenuItemsForAdmin() {
        Restaurant restaurant = restaurant();
        MenuItem menuItem = menuItem(restaurant);
        menuItem.changeAvailability(false);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findByRestaurantId(1L)).thenReturn(List.of(menuItem));

        List<MenuItem> result = menuItemService.getAllMenuItemsForAdmin(1L);

        assertEquals(1, result.size());
        assertEquals(false, result.get(0).isAvailable());
    }

    @Test
    void rejectsNegativePrice() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant()));

        assertThrows(
                InvalidMenuItemPriceException.class,
                () -> menuItemService.addMenuItem(
                        1L,
                        "Margherita",
                        "Tomato, mozzarella and basil",
                        new BigDecimal("-0.01"),
                        "Pizza"
                )
        );
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void rejectsMissingPrice() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant()));

        assertThrows(
                InvalidMenuItemPriceException.class,
                () -> menuItemService.addMenuItem(
                        1L,
                        "Margherita",
                        "Tomato, mozzarella and basil",
                        null,
                        "Pizza"
                )
        );
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void rejectsDuplicateMenuItemNameAfterTrimming() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant()));
        when(menuItemRepository.existsByRestaurantIdAndNameIgnoreCase(1L, "Margherita"))
                .thenReturn(true);

        assertThrows(
                DuplicateMenuItemException.class,
                () -> menuItemService.addMenuItem(
                        1L,
                        "  Margherita  ",
                        "Tomato, mozzarella and basil",
                        new BigDecimal("32.00"),
                        "Pizza"
                )
        );
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void trimsMenuItemTextBeforeSaving() {
        Restaurant restaurant = restaurant();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.existsByRestaurantIdAndNameIgnoreCase(1L, "Margherita"))
                .thenReturn(false);
        when(menuItemRepository.save(any(MenuItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MenuItem result = menuItemService.addMenuItem(
                1L,
                "  Margherita  ",
                "  Tomato, mozzarella and basil  ",
                new BigDecimal("32.00"),
                "  Pizza  "
        );

        assertSame(restaurant, result.getRestaurant());
        assertEquals("Margherita", result.getName());
        assertEquals("Tomato, mozzarella and basil", result.getDescription());
        assertEquals("Pizza", result.getCategory());
    }

    @Test
    void changesMenuItemAvailability() {
        Restaurant restaurant = restaurant();
        MenuItem item = menuItem(restaurant);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findByIdAndRestaurantId(2L, 1L))
                .thenReturn(Optional.of(item));

        MenuItem result = menuItemService.changeAvailability(1L, 2L, false);

        assertEquals(false, result.isAvailable());
    }

    private Restaurant restaurant() {
        return new Restaurant("Urban Pizza", "10 Main Street", "Bucharest");
    }

    private MenuItem menuItem(Restaurant restaurant) {
        return new MenuItem(
                restaurant,
                "Margherita",
                "Tomato, mozzarella and basil",
                new BigDecimal("32.00"),
                "Pizza"
        );
    }
}
