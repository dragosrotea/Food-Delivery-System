package com.dragosrotea.fooddelivery.common.api;

import com.dragosrotea.fooddelivery.order.exception.*;
import com.dragosrotea.fooddelivery.restaurant.exception.*;
import com.dragosrotea.fooddelivery.user.exception.EmailAlreadyRegisteredException;
import com.dragosrotea.fooddelivery.user.exception.InvalidCredentialsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            RestaurantNotFoundException.class,
            OrderNotFoundException.class,
            MenuItemNotFoundException.class,
            MenuItemNotFoundInRestaurantException.class
    })
    public ResponseEntity<ApiError> handleNotFound(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.NOT_FOUND, exception.getMessage(), request, Map.of()
        );
    }

    @ExceptionHandler({
            DuplicateRestaurantException.class,
            DuplicateMenuItemException.class,
            EmailAlreadyRegisteredException.class,
            InvalidOrderStatusTransitionException.class,
            MenuItemUnavailableException.class,
            MenuItemRestaurantMismatchException.class,
            RestaurantUnavailableException.class,
            DriverOrderUnavailableException.class
    })
    public ResponseEntity<ApiError> handleConflict(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.CONFLICT, exception.getMessage(), request, Map.of()
        );
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ApiError> handleConcurrentUpdate(
            OptimisticLockingFailureException exception,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.CONFLICT,
                "The order was changed by another request. Refresh and try again",
                request,
                Map.of()
        );
    }

    @ExceptionHandler({
            OrderAccessDeniedException.class,
            DriverOrderAccessDeniedException.class,
            DriverAccountRequiredException.class
    })
    public ResponseEntity<ApiError> handleForbidden(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.FORBIDDEN, exception.getMessage(), request, Map.of()
        );
    }

    @ExceptionHandler({
            InvalidMenuItemPriceException.class,
            EmptyOrderException.class,
            InvalidOrderQuantityException.class,
            InvalidCredentialsException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(
            RuntimeException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = exception instanceof InvalidCredentialsException
                ? HttpStatus.UNAUTHORIZED
                : HttpStatus.BAD_REQUEST;
        return buildError(status, exception.getMessage(), request, Map.of());
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
