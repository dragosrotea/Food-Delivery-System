package com.dragosrotea.fooddelivery.order.api;

import com.dragosrotea.fooddelivery.order.FoodOrder;
import com.dragosrotea.fooddelivery.order.OrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long customerId,
        String customerEmail,
        Long restaurantId,
        String restaurantName,
        OrderStatus status,
        BigDecimal totalPrice,
        String deliveryStreet,
        String deliveryCity,
        OffsetDateTime createdAt,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(FoodOrder order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getCustomer().getEmail(),
                order.getRestaurant().getId(),
                order.getRestaurant().getName(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getDeliveryStreet(),
                order.getDeliveryCity(),
                order.getCreatedAt(),
                order.getItems().stream().map(OrderItemResponse::from).toList()
        );
    }
}
