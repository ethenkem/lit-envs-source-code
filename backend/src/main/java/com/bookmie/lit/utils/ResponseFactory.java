package com.bookmie.lit.utils;

import com.bookmie.lit.utils.dtos.ApiError;
import com.bookmie.lit.utils.dtos.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public final class ResponseFactory {

    private ResponseFactory() {
    }

    // Success responses

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(success(message, data));
    }

    public static ResponseEntity<ApiResponse<Void>> ok(String message) {
        return ResponseEntity.ok(success(message, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(success(message, data));
    }

    // Error responses

    public static ResponseEntity<ApiResponse<Void>> error(
            HttpStatus status, String message, String path) {
        return error(status, message, path, null);
    }

    public static ResponseEntity<ApiResponse<Void>> error(
            HttpStatus status, String message, String path, List<String> errors) {

        ApiError apiError = ApiError.builder()
                .status(status.value())
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(false)
                .message(message)
                .error(apiError)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(response);
    }

    // Internal builders

    private static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
