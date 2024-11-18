package com.hhplus.server.domain.support.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

import static org.springframework.boot.logging.LogLevel.WARN;


@Getter
@RequiredArgsConstructor
public enum WaitingQueueErrorCode implements ErrorCode{
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.", WARN),
    NOT_ACTIVE_TOKEN(HttpStatus.FORBIDDEN, "접근 불가능한 상태의 토큰입니다.", WARN),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.", WARN)
    ;

    private final HttpStatus status;
    private final String message;
    private final LogLevel logLevel;
}
