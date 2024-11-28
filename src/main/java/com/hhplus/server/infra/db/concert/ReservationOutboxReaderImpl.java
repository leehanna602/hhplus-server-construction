package com.hhplus.server.infra.db.concert;

import com.hhplus.server.domain.base.OutBoxStatus;
import com.hhplus.server.domain.concert.ReservationOutboxReader;
import com.hhplus.server.domain.concert.model.ReservationOutbox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationOutboxReaderImpl implements ReservationOutboxReader {

    private final ReservationOutboxJpaRepository reservationOutboxJpaRepository;

    @Override
    public ReservationOutbox findByReservationIdWithOptimisticLock(Long reservationId) {
        return reservationOutboxJpaRepository.findByReservationId(reservationId);
    }

    @Override
    public List<ReservationOutbox> findAllByOutboxStatusAndCreatedAtAfter(OutBoxStatus outBoxStatus, LocalDateTime localDateTime) {
        return reservationOutboxJpaRepository.findAllByOutboxStatusAndCreatedAtAfter(outBoxStatus, localDateTime);
    }

}
