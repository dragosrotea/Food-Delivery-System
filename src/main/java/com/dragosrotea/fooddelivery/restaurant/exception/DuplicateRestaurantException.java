package com.dragosrotea.fooddelivery.restaurant.exception;

public class DuplicateRestaurantException extends RuntimeException {

    public DuplicateRestaurantException(String name) {
        super("A restaurant named '" + name + "' already exists");
    }
}
