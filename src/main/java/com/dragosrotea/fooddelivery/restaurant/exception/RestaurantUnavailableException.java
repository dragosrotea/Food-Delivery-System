package com.dragosrotea.fooddelivery.restaurant.exception;

public class RestaurantUnavailableException extends RuntimeException {
    public RestaurantUnavailableException(Long restaurantId) {
        super("Restaurant with id " + restaurantId + " is not accepting orders");
    }
}
