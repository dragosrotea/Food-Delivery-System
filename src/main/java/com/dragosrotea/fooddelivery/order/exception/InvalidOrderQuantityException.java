package com.dragosrotea.fooddelivery.order.exception;

public class InvalidOrderQuantityException extends RuntimeException {
    public InvalidOrderQuantityException() {
        super("Order item quantity must be greater than zero");
    }
}
