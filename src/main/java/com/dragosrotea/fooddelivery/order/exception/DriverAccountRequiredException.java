package com.dragosrotea.fooddelivery.order.exception;

public class DriverAccountRequiredException extends RuntimeException {

    public DriverAccountRequiredException() {
        super("A driver account is required");
    }
}
