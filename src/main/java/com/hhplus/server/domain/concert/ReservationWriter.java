package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.model.Reservation;

public interface ReservationWriter {
    Reservation save(Reservation reservation);
}
