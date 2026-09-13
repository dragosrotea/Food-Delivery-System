package com.dragosrotea.fooddelivery.restaurant.api;

import com.dragosrotea.fooddelivery.restaurant.Restaurant;

public record RestaurantResponse(
        Long id,
        String name,
        String street,
        String city
) {

    public static RestaurantResponse from(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getStreet(),
                restaurant.getCity()
        );
    }
}
