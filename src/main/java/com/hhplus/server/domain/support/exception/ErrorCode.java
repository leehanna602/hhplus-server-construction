package com.hhplus.server.domain.support.exception;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getStatus();
    String getMessage();
    LogLevel getLogLevel();
}
