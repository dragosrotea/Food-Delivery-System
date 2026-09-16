package com.dragosrotea.fooddelivery.driver;

import com.dragosrotea.fooddelivery.order.FoodOrder;
import com.dragosrotea.fooddelivery.order.OrderStatus;
import com.dragosrotea.fooddelivery.order.api.OrderItemResponse;
import com.dragosrotea.fooddelivery.user.UserAccount;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record DriverDeliveryResponse(
        Long id,
        Long restaurantId,
        String restaurantName,
        String restaurantStreet,
        String restaurantCity,
        Long driverId,
        String driverEmail,
        OrderStatus status,
        BigDecimal totalPrice,
        String deliveryStreet,
        String deliveryCity,
        OffsetDateTime createdAt,
        List<OrderItemResponse> items
) {
    public static DriverDeliveryResponse from(FoodOrder order) {
        UserAccount driver = order.getDriver();

        return new DriverDeliveryResponse(
                order.getId(),
                order.getRestaurant().getId(),
                order.getRestaurant().getName(),
                order.getRestaurant().getStreet(),
                order.getRestaurant().getCity(),
                driver == null ? null : driver.getId(),
                driver == null ? null : driver.getEmail(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getDeliveryStreet(),
                order.getDeliveryCity(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(OrderItemResponse::from)
                        .toList()
        );
    }
}
