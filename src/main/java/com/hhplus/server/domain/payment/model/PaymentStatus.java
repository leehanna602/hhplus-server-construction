package com.hhplus.server.domain.payment.model;

/**
 * 결제 상태 종류
 * - PENDING : 대기
 * - COMPLETED : 완료
 * - FAILED : 실패
 */
public enum PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED
}
