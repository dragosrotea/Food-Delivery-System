package com.dragosrotea.fooddelivery.order.exception;

public class DriverOrderUnavailableException extends RuntimeException {

    public DriverOrderUnavailableException(Long orderId) {
        super("Order " + orderId + " is not available for driver assignment");
    }
}
