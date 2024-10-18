package com.hhplus.server.domain.concert.dto;

import com.hhplus.server.domain.concert.model.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationInfo(
        Long reservationId,
        Long seatId,
        ReservationStatus status,
        LocalDateTime expiredDt
) {
}
