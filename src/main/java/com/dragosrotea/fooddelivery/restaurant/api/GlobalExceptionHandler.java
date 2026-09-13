package com.dragosrotea.fooddelivery.restaurant.api;

import com.dragosrotea.fooddelivery.restaurant.exception.DuplicateMenuItemException;
import com.dragosrotea.fooddelivery.restaurant.exception.DuplicateRestaurantException;
import com.dragosrotea.fooddelivery.restaurant.exception.InvalidMenuItemPriceException;
import com.dragosrotea.fooddelivery.restaurant.exception.RestaurantNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ApiError> handleRestaurantNotFound(
            RestaurantNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler({
            DuplicateRestaurantException.class,
            DuplicateMenuItemException.class
    })
    public ResponseEntity<ApiError> handleDuplicateResource(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.CONFLICT, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(InvalidMenuItemPriceException.class)
    public ResponseEntity<ApiError> handleInvalidPrice(
            InvalidMenuItemPriceException exception,
            HttpServletRequest request
    ) {
        return buildError(HttpStatus.BAD_REQUEST, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationFailure(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage())
        );

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Request validation failed",
                request,
                fieldErrors
        );
    }

    private ResponseEntity<ApiError> buildError(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            Map<String, String> fieldErrors
    ) {
        ApiError error = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity.status(status).body(error);
    }
}
