package com.dragosrotea.fooddelivery.restaurant;

import com.dragosrotea.fooddelivery.restaurant.exception.DuplicateRestaurantException;
import com.dragosrotea.fooddelivery.restaurant.exception.RestaurantNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void returnsRestaurantWhenItExists() {
        Restaurant restaurant = new Restaurant("Urban Pizza", "10 Main Street", "Bucharest");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantService.getRestaurant(1L);

        assertEquals("Urban Pizza", result.getName());
    }

    @Test
    void returnsActiveRestaurantForPublicAccess() {
        Restaurant restaurant = new Restaurant("Urban Pizza", "10 Main Street", "Bucharest");
        when(restaurantRepository.findByIdAndActiveTrue(1L))
                .thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantService.getActiveRestaurant(1L);

        assertEquals("Urban Pizza", result.getName());
    }

    @Test
    void hidesInactiveRestaurantFromPublicAccess() {
        when(restaurantRepository.findByIdAndActiveTrue(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                RestaurantNotFoundException.class,
                () -> restaurantService.getActiveRestaurant(1L)
        );
    }

    @Test
    void throwsExceptionWhenRestaurantDoesNotExist() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                RestaurantNotFoundException.class,
                () -> restaurantService.getRestaurant(99L)
        );
    }

    @Test
    void rejectsDuplicateRestaurantNameAfterTrimming() {
        when(restaurantRepository.existsByNameIgnoreCase("Urban Pizza")).thenReturn(true);

        assertThrows(
                DuplicateRestaurantException.class,
                () -> restaurantService.createRestaurant(
                        "  Urban Pizza  ",
                        "10 Main Street",
                        "Bucharest"
                )
        );
        verify(restaurantRepository, never()).save(any(Restaurant.class));
    }

    @Test
    void trimsNewRestaurantDetailsBeforeSaving() {
        when(restaurantRepository.existsByNameIgnoreCase("Urban Pizza")).thenReturn(false);
        when(restaurantRepository.save(any(Restaurant.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Restaurant result = restaurantService.createRestaurant(
                "  Urban Pizza  ",
                "  10 Main Street  ",
                "  Bucharest  "
        );

        assertEquals("Urban Pizza", result.getName());
        assertEquals("10 Main Street", result.getStreet());
        assertEquals("Bucharest", result.getCity());

        ArgumentCaptor<Restaurant> captor = ArgumentCaptor.forClass(Restaurant.class);
        verify(restaurantRepository).save(captor.capture());
        assertEquals("Urban Pizza", captor.getValue().getName());
    }

    @Test
    void changesRestaurantAvailabilityWithoutDeletingIt() {
        Restaurant restaurant = new Restaurant("Urban Pizza", "10 Main Street", "Bucharest");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantService.changeAvailability(1L, false);

        assertEquals(false, result.isActive());
        verify(restaurantRepository, never()).delete(any(Restaurant.class));
    }

    @Test
    void trimsUpdatedRestaurantDetails() {
        Restaurant restaurant = new Restaurant("Urban Pizza", "10 Main Street", "Bucharest");
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.existsByNameIgnoreCaseAndIdNot("New Name", 1L))
                .thenReturn(false);

        Restaurant result = restaurantService.updateRestaurant(
                1L,
                "  New Name  ",
                "  20 Main Street  ",
                "  Cluj  "
        );

        assertEquals("New Name", result.getName());
        assertEquals("20 Main Street", result.getStreet());
        assertEquals("Cluj", result.getCity());
    }
}
