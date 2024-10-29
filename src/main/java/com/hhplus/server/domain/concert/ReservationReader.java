package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.model.Reservation;

import java.util.List;

public interface ReservationReader {
    Reservation findByReservationId(Long reservationId);

    List<Reservation> findReservationsTemporaryToExpired();

}
