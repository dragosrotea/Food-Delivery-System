package com.dragosrotea.fooddelivery.order.exception;

import com.dragosrotea.fooddelivery.order.OrderStatus;

public class InvalidOrderStatusTransitionException extends RuntimeException {
    public InvalidOrderStatusTransitionException(OrderStatus current, OrderStatus requested) {
        super("Order status cannot change from " + current + " to " + requested);
    }
}
