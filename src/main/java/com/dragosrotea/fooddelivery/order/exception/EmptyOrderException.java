package com.dragosrotea.fooddelivery.order.exception;

public class EmptyOrderException extends RuntimeException {
    public EmptyOrderException() {
        super("An order must contain at least one item");
    }
}
