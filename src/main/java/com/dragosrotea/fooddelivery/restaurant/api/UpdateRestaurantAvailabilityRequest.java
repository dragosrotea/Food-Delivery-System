package com.dragosrotea.fooddelivery.restaurant.api;

import jakarta.validation.constraints.NotNull;

public record UpdateRestaurantAvailabilityRequest(
        @NotNull(message = "Active status is required") Boolean active
) {}
