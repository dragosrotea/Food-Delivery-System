package com.dragosrotea.fooddelivery.restaurant.exception;

public class MenuItemNotFoundInRestaurantException extends RuntimeException {
    public MenuItemNotFoundInRestaurantException(Long menuItemId, Long restaurantId) {
        super("Menu item with id " + menuItemId + " was not found in restaurant " + restaurantId);
    }
}
