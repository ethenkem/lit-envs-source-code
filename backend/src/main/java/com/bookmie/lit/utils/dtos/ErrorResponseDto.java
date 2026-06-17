package com.bookmie.lit.utils.dtos;

public record ErrorResponseDto(
    Integer statusCode,
    String message,
    Object details
) {}
