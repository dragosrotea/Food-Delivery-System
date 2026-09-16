package com.dragosrotea.fooddelivery.driver;

import com.dragosrotea.fooddelivery.auth.UserResponse;
import com.dragosrotea.fooddelivery.user.UserAccount;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Drivers")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/admin/drivers")
public class AdminDriverController {

    private final DriverAccountService driverAccountService;

    public AdminDriverController(DriverAccountService driverAccountService) {
        this.driverAccountService = driverAccountService;
    }

    @Operation(summary = "Create a driver account")
    @PostMapping
    public ResponseEntity<UserResponse> createDriver(
            @Valid @RequestBody CreateDriverRequest request
    ) {
        UserAccount driver = driverAccountService.createDriver(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.from(driver));
    }
}
