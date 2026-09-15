package com.dragosrotea.fooddelivery.driver;

import com.dragosrotea.fooddelivery.order.OrderService;
import com.dragosrotea.fooddelivery.order.api.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Driver Orders")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/driver/orders")
public class DriverOrderController {

    private final OrderService orderService;

    public DriverOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "List orders ready for driver assignment")
    @GetMapping("/available")
    public List<OrderResponse> getAvailableDeliveries() {
        return orderService.getAvailableDeliveries().stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Operation(summary = "List orders assigned to the current driver")
    @GetMapping
    public List<OrderResponse> getMyDeliveries(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return orderService.getDriverOrders(jwt.getSubject()).stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Operation(summary = "Accept an available delivery")
    @PostMapping("/{orderId}/accept")
    public OrderResponse acceptDelivery(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId
    ) {
        return OrderResponse.from(
                orderService.acceptDelivery(jwt.getSubject(), orderId)
        );
    }

    @Operation(summary = "Complete an assigned delivery")
    @PostMapping("/{orderId}/complete")
    public OrderResponse completeDelivery(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId
    ) {
        return OrderResponse.from(
                orderService.completeDelivery(jwt.getSubject(), orderId)
        );
    }
}
