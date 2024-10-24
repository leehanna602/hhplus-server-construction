package com.hhplus.server.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.boot.logging.LogLevel.WARN;


@Getter
@RequiredArgsConstructor
public enum ConcertErrorCode implements ErrorCode {
    INVALID_CONCERT(HttpStatus.NOT_FOUND, "유효하지 않은 콘서트입니다.", WARN),
    INVALID_CONCERT_SCHEDULE(HttpStatus.BAD_REQUEST, "유효하지 않은 콘서트 일정입니다.", WARN),
    INVALID_SEAT(HttpStatus.BAD_REQUEST, "유효하지 않은 좌석입니다.", WARN),
    UNAVAILABLE_SEAT(HttpStatus.CONFLICT, "예약이 불가능한 좌석입니다.", WARN),
    EXPIRED_RESERVATION(HttpStatus.CONFLICT, "만료된 예약 정보입니다.", WARN)
    ;

    private final HttpStatus status;
    private final String message;
    private final LogLevel logLevel;
}
