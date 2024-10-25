package com.hhplus.server.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.boot.logging.LogLevel.WARN;


@Getter
@RequiredArgsConstructor
public enum PaymentErrorCode implements ErrorCode{
    EXCEEDED_CHARGE_AMOUNT(HttpStatus.BAD_REQUEST, "충전 가능한 금액이 초과되었습니다.", WARN),
    INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "포인트가 부족합니다.", WARN),
    FAILED_PAYMENT(HttpStatus.INTERNAL_SERVER_ERROR, "결재에 실패했습니다.", ERROR)
    ;

    private final HttpStatus status;
    private final String message;
    private final LogLevel logLevel;
}
