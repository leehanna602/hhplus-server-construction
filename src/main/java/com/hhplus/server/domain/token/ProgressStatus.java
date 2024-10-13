package com.hhplus.server.domain.token;

/**
 * 대기열 진행 상태 종류
 * - WAITING : 대기
 * - PROCESSING : 진행
 * - COMPLETED : 완료
 * - EXPIRED : 만료
 */
public enum ProgressStatus {
    WAITING,
    PROCESSING,
    COMPLETED,
    EXPIRED
}
