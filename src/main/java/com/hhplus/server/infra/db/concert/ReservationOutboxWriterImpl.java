package com.hhplus.server.infra.db.concert;

import com.hhplus.server.domain.concert.ReservationOutboxWriter;
import com.hhplus.server.domain.concert.model.ReservationOutbox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationOutboxWriterImpl implements ReservationOutboxWriter {

    private final ReservationOutboxJpaRepository reservationOutboxJpaRepository;

    @Override
    public ReservationOutbox save(ReservationOutbox reservationOutBox) {
        return reservationOutboxJpaRepository.save(reservationOutBox);
    }
}
