package com.dragosrotea.fooddelivery.order.exception;

public class MenuItemNotFoundException extends RuntimeException {
    public MenuItemNotFoundException(Long menuItemId) {
        super("Menu item with id " + menuItemId + " was not found");
    }
}
