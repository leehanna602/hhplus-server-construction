package com.hhplus.server.domain.waitingQueue.model;

/**
 * 대기열 진행 상태 종류
 * - WAITING : 대기
 * - ACTIVE : 활성화
 * - EXPIRED : 만료
 */
public enum ProgressStatus {
    WAITING,
    ACTIVE,
    EXPIRED
}
