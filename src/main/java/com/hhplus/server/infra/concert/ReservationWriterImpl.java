package com.hhplus.server.infra.concert;

import com.hhplus.server.domain.concert.ReservationWriter;
import com.hhplus.server.domain.concert.model.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationWriterImpl implements ReservationWriter {

    private final ReservationJpaRepository reservationJpaRepository;

    @Override
    public Reservation save(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }
}
