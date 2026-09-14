package com.dragosrotea.fooddelivery.order.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderItemRequest(
        @NotNull(message = "Menu item id is required")
        Long menuItemId,

        @Positive(message = "Quantity must be greater than zero")
        int quantity
) {
}
