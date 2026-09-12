package com.dragosrotea.fooddelivery.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    void savesRestaurantAndFindsItsAvailableMenuItems() {
        Restaurant restaurant = new Restaurant(
                "Urban Pizza",
                "Central Street 10",
                "Cluj-Napoca"
        );

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        MenuItem menuItem = new MenuItem(
                savedRestaurant,
                "Diavola",
                "Spicy salami pizza",
                new BigDecimal("39.50"),
                "Pizza"
        );

        menuItemRepository.save(menuItem);

        List<MenuItem> availableItems =
                menuItemRepository.findByRestaurantIdAndAvailableTrue(
                        savedRestaurant.getId()
                );

        assertEquals(1, availableItems.size());
        assertEquals("Diavola", availableItems.get(0).getName());
        assertEquals(
                new BigDecimal("39.50"),
                availableItems.get(0).getPrice()
        );
        assertTrue(
                restaurantRepository.existsByNameIgnoreCase("urban pizza")
        );
    }
}
