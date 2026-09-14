package com.dragosrotea.fooddelivery.order.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOrderRequest(
        @NotNull(message = "Restaurant id is required")
        Long restaurantId,

        @NotBlank(message = "Delivery street is required")
        @Size(max = 160, message = "Delivery street must contain at most 160 characters")
        String deliveryStreet,

        @NotBlank(message = "Delivery city is required")
        @Size(max = 100, message = "Delivery city must contain at most 100 characters")
        String deliveryCity,

        @NotEmpty(message = "At least one order item is required")
        List<@Valid CreateOrderItemRequest> items
) {
}
