package com.dragosrotea.fooddelivery.restaurant.exception;

public class RestaurantNotFoundException extends RuntimeException {

    public RestaurantNotFoundException(Long restaurantId) {
        super("Restaurant with id " + restaurantId + " was not found");
    }
}
