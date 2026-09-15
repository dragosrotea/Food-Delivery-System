package com.dragosrotea.fooddelivery.order.api;

import com.dragosrotea.fooddelivery.order.FoodOrder;
import com.dragosrotea.fooddelivery.order.OrderStatus;
import com.dragosrotea.fooddelivery.user.UserAccount;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long customerId,
        String customerEmail,
        Long restaurantId,
        String restaurantName,
        Long driverId,
        String driverEmail,
        OrderStatus status,
        BigDecimal totalPrice,
        String deliveryStreet,
        String deliveryCity,
        OffsetDateTime createdAt,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(FoodOrder order) {
        UserAccount driver = order.getDriver();

        return new OrderResponse(
                order.getId(),
                order.getCustomer().getId(),
                order.getCustomer().getEmail(),
                order.getRestaurant().getId(),
                order.getRestaurant().getName(),
                driver == null ? null : driver.getId(),
                driver == null ? null : driver.getEmail(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getDeliveryStreet(),
                order.getDeliveryCity(),
                order.getCreatedAt(),
                order.getItems().stream().map(OrderItemResponse::from).toList()
        );
    }
}
