package com.dragosrotea.fooddelivery.restaurant.exception;

import java.math.BigDecimal;

public class InvalidMenuItemPriceException extends RuntimeException {

    public InvalidMenuItemPriceException(BigDecimal price) {
        super("Menu item price must be zero or greater, but was " + price);
    }
}
