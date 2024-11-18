package com.hhplus.server.domain.support.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.boot.logging.LogLevel.WARN;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 입력입니다.", WARN),
    DISTRIBUTED_LOCK_ACQUISITION_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "분산 락 획득에 실패했습니다.", ERROR)
    ;

    private final HttpStatus status;
    private final String message;
    private final LogLevel logLevel;
}
