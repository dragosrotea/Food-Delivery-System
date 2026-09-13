package com.dragosrotea.fooddelivery.restaurant.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRestaurantRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 120, message = "Name must contain at most 120 characters")
        String name,

        @NotBlank(message = "Street is required")
        @Size(max = 160, message = "Street must contain at most 160 characters")
        String street,

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City must contain at most 100 characters")
        String city
) {
}
