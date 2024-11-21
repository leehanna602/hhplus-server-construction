package com.hhplus.server.infra.db.concert;

import com.hhplus.server.domain.concert.ReservationReader;
import com.hhplus.server.domain.concert.model.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationReaderImpl implements ReservationReader {

    private final ReservationJpaRepository reservationJpaRepository;

    @Override
    public Reservation findByReservationId(Long reservationId) {
        return reservationJpaRepository.findByReservationId(reservationId);
    }

    @Override
    public List<Reservation> findReservationsTemporaryToExpired() {
        return reservationJpaRepository.findReservationsTemporaryToExpired();
    }

}
