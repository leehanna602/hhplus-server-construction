package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.model.ReservationOutbox;

public interface ReservationOutboxReader {

    ReservationOutbox findByReservationIdWithOptimisticLock(Long reservationId);

}
