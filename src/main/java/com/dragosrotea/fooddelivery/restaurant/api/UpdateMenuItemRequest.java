package com.dragosrotea.fooddelivery.restaurant.api;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record UpdateMenuItemRequest(
        @NotBlank(message = "Name is required") @Size(max = 120) String name,
        @Size(max = 500) String description,
        @NotNull(message = "Price is required") @PositiveOrZero(message = "Price must be zero or greater") BigDecimal price,
        @NotBlank(message = "Category is required") @Size(max = 60) String category
) {}
