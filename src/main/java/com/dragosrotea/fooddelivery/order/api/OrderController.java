package com.dragosrotea.fooddelivery.order.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import com.dragosrotea.fooddelivery.order.FoodOrder;
import com.dragosrotea.fooddelivery.order.OrderLineCommand;
import com.dragosrotea.fooddelivery.order.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Customer Orders")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Place an order")
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateOrderRequest request
    ) {
        List<OrderLineCommand> lines = request.items().stream()
                .map(item -> new OrderLineCommand(item.menuItemId(), item.quantity()))
                .toList();

        FoodOrder order = orderService.placeOrder(
                jwt.getSubject(),
                request.restaurantId(),
                request.deliveryStreet(),
                request.deliveryCity(),
                lines
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponse.from(order));
    }

    @Operation(summary = "List the current customer's orders")
    @GetMapping
    public List<OrderResponse> getMyOrders(@AuthenticationPrincipal Jwt jwt) {
        return orderService.getCustomerOrders(jwt.getSubject())
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Operation(summary = "Get an accessible order")
    @GetMapping("/{orderId}")
    public OrderResponse getOrder(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId
    ) {
        boolean admin = "ADMIN".equals(jwt.getClaimAsString("role"));
        return OrderResponse.from(orderService.getOrder(jwt.getSubject(), admin, orderId));
    }

    @Operation(summary = "Cancel an eligible order")
    @PostMapping("/{orderId}/cancel")
    public OrderResponse cancelOrder(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long orderId
    ) {
        return OrderResponse.from(orderService.cancelOrder(jwt.getSubject(), orderId));
    }
}
