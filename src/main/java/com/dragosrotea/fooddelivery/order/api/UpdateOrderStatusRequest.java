package com.dragosrotea.fooddelivery.order.api;

import com.dragosrotea.fooddelivery.order.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(
        @NotNull(message = "Status is required")
        OrderStatus status
) {
}
