package com.hhplus.server.interfaces.support.advice;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        HttpStatus status,
        String name,
        String message
) {
}
