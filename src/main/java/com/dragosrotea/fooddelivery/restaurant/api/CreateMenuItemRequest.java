package com.dragosrotea.fooddelivery.restaurant.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateMenuItemRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 120, message = "Name must contain at most 120 characters")
        String name,

        @Size(max = 500, message = "Description must contain at most 500 characters")
        String description,

        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price must be zero or greater")
        BigDecimal price,

        @NotBlank(message = "Category is required")
        @Size(max = 60, message = "Category must contain at most 60 characters")
        String category
) {
}
