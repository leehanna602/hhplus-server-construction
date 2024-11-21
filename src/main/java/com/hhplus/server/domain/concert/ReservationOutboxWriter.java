package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.model.ReservationOutbox;

public interface ReservationOutboxWriter {
    ReservationOutbox save(ReservationOutbox reservationOutBox);
}
