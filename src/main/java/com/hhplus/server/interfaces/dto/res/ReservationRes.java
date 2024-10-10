package com.hhplus.server.interfaces.dto.res;

import com.hhplus.server.domain.reservation.ReservationStatus;

public record ReservationRes(
        Long reservationId,
        Long seatId,
        ReservationStatus status,
        String expiredDt
) {
}
