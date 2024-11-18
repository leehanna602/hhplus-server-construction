package com.hhplus.server.domain.support.exception;

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
    POINT_USE_LOCK_ACQUISITION_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "포인트 사용 락 획득 실패", ERROR),
    POINT_CHARGE_LOCK_ACQUISITION_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "포인트 충전 락 획득 실패", ERROR),
    POINT_USE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "포인트 사용 중 에러 발생", ERROR),
    POINT_CHARGE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "포인트 사용 중 에러 발생", ERROR),
    FAILED_PAYMENT(HttpStatus.INTERNAL_SERVER_ERROR, "결제에 실패했습니다.", ERROR)
    ;

    private final HttpStatus status;
    private final String message;
    private final LogLevel logLevel;
}
