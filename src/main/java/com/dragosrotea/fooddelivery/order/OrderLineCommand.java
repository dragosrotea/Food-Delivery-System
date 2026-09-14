package com.dragosrotea.fooddelivery.order;

public record OrderLineCommand(Long menuItemId, int quantity) {
}
