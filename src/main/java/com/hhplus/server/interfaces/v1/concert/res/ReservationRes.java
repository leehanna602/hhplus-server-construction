package com.hhplus.server.interfaces.v1.concert.res;

import com.hhplus.server.domain.concert.model.ReservationStatus;

public record ReservationRes(
        Long reservationId,
        Long seatId,
        ReservationStatus status,
        String expiredDt
) {
}
