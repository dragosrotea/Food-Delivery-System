package com.dragosrotea.fooddelivery.order.exception;

public class MenuItemUnavailableException extends RuntimeException {
    public MenuItemUnavailableException(Long menuItemId) {
        super("Menu item with id " + menuItemId + " is not available");
    }
}
