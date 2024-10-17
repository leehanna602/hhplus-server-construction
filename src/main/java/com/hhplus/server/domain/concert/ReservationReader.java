package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.model.Reservation;

public interface ReservationReader {
    Reservation findByReservationId(Long reservationId);
}
