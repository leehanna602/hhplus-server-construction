package com.hhplus.server.infra.db.concert;

import com.hhplus.server.domain.concert.ReservationOutboxReader;
import com.hhplus.server.domain.concert.model.ReservationOutbox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationOutboxReaderImpl implements ReservationOutboxReader {

    private final ReservationOutboxJpaRepository reservationOutboxJpaRepository;

    @Override
    public ReservationOutbox findByReservationIdWithOptimisticLock(Long reservationId) {
        return reservationOutboxJpaRepository.findByReservationId(reservationId);
    }

}
