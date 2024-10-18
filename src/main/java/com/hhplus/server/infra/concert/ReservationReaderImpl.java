package com.hhplus.server.infra.concert;

import com.hhplus.server.domain.concert.ReservationReader;
import com.hhplus.server.domain.concert.model.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationReaderImpl implements ReservationReader {

    private final ReservationJpaRepository reservationJpaRepository;

    @Override
    public Reservation findByReservationId(Long reservationId) {
        return reservationJpaRepository.findByReservationId(reservationId);
    }

}
