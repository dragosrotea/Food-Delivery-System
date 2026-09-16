package com.dragosrotea.fooddelivery.driver;

import com.dragosrotea.fooddelivery.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<AvailableDeliveryResponse> getAvailableDeliveries() {
        return orderService.getAvailableDeliveries().stream()
                .map(AvailableDeliveryResponse::from)
                .toList();
    }

    @Operation(summary = "List orders assigned to the current driver")
    @GetMapping
    public List<DriverDeliveryResponse> getMyDeliveries(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return orderService.getDriverOrders(jwt.getSubject()).stream()
                .map(DriverDeliveryResponse::from)
                .toList();
    }

    @Operation(summary = "Accept an available delivery")
    @PostMapping("/{orderId}/accept")
    public DriverDeliveryResponse acceptDelivery(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId
    ) {
        return DriverDeliveryResponse.from(
                orderService.acceptDelivery(jwt.getSubject(), orderId)
        );
    }

    @Operation(summary = "Complete an assigned delivery")
    @PostMapping("/{orderId}/complete")
    public DriverDeliveryResponse completeDelivery(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId
    ) {
        return DriverDeliveryResponse.from(
                orderService.completeDelivery(jwt.getSubject(), orderId)
        );
    }
}
