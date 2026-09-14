package com.dragosrotea.fooddelivery.restaurant.api;

import jakarta.validation.constraints.NotNull;

public record UpdateMenuItemAvailabilityRequest(
        @NotNull(message = "Available status is required") Boolean available
) {}
