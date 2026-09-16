package com.dragosrotea.fooddelivery.order.exception;

public class DriverOrderAccessDeniedException extends RuntimeException {

    public DriverOrderAccessDeniedException(Long orderId) {
        super("You are not the assigned driver for order " + orderId);
    }
}
