package com.hhplus.server.interfaces.support.advice;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ErrorResponse(
        HttpStatus status,
        String name,
        String message
) {
}
