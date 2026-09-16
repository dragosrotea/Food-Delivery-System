package com.dragosrotea.fooddelivery.driver;

import com.dragosrotea.fooddelivery.order.FoodOrder;
import com.dragosrotea.fooddelivery.order.OrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record AvailableDeliveryResponse(
        Long id,
        Long restaurantId,
        String restaurantName,
        String restaurantStreet,
        String restaurantCity,
        String deliveryCity,
        OrderStatus status,
        BigDecimal totalPrice,
        int itemCount,
        OffsetDateTime createdAt
) {
    public static AvailableDeliveryResponse from(FoodOrder order) {
        return new AvailableDeliveryResponse(
                order.getId(),
                order.getRestaurant().getId(),
                order.getRestaurant().getName(),
                order.getRestaurant().getStreet(),
                order.getRestaurant().getCity(),
                order.getDeliveryCity(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getItems().size(),
                order.getCreatedAt()
        );
    }
}
