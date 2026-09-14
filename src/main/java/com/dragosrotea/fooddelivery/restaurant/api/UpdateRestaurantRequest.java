package com.dragosrotea.fooddelivery.restaurant.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateRestaurantRequest(
        @NotBlank(message = "Name is required") @Size(max = 120) String name,
        @NotBlank(message = "Street is required") @Size(max = 160) String street,
        @NotBlank(message = "City is required") @Size(max = 100) String city
) {}
