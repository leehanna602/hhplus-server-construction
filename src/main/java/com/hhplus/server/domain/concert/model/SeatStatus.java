package com.hhplus.server.domain.concert.model;

/**
 * 좌석 상태 종류
 * - AVAILABLE : 이용가능
 * - TEMPORARY_RESERVED : 임시예약
 * - RESERVED : 예약완료
 */
public enum SeatStatus {
    AVAILABLE,
    TEMPORARY_RESERVED,
    RESERVED
}
