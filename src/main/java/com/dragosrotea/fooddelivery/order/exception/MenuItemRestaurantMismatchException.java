package com.dragosrotea.fooddelivery.order.exception;

public class MenuItemRestaurantMismatchException extends RuntimeException {
    public MenuItemRestaurantMismatchException(Long menuItemId, Long restaurantId) {
        super("Menu item with id " + menuItemId + " does not belong to restaurant " + restaurantId);
    }
}
