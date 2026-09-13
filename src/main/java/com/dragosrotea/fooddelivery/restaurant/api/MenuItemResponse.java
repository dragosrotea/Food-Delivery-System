package com.dragosrotea.fooddelivery.restaurant.api;

import com.dragosrotea.fooddelivery.restaurant.MenuItem;

import java.math.BigDecimal;

public record MenuItemResponse(
        Long id,
        Long restaurantId,
        String name,
        String description,
        BigDecimal price,
        String category,
        boolean available
) {

    public static MenuItemResponse from(MenuItem menuItem) {
        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getRestaurant().getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getCategory(),
                menuItem.isAvailable()
        );
    }
}
