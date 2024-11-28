package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.base.OutBoxStatus;
import com.hhplus.server.domain.concert.model.ReservationOutbox;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationOutboxReader {

    ReservationOutbox findByReservationIdWithOptimisticLock(Long reservationId);

    List<ReservationOutbox> findAllByOutboxStatusAndCreatedAtAfter(OutBoxStatus outBoxStatus, LocalDateTime localDateTime);
}
