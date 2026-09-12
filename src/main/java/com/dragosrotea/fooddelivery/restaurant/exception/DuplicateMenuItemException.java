package com.dragosrotea.fooddelivery.restaurant.exception;

public class DuplicateMenuItemException extends RuntimeException {

    public DuplicateMenuItemException(Long restaurantId, String name) {
        super("Menu item '" + name + "' already exists for restaurant " + restaurantId);
    }
}
