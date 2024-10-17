package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.domain.concert.model.ConcertSeat;
import com.hhplus.server.domain.concert.model.Reservation;
import com.hhplus.server.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationReader reservationReader;
    private final ReservationWriter reservationWriter;

    @Transactional
    public ReservationInfo concertSeatTemporalReservation(User user, ConcertSeat concertSeat) {
        Reservation reservation = new Reservation(user, concertSeat);
        reservation = reservationWriter.save(reservation);
        ReservationInfo reservationInfo = new ReservationInfo(
                reservation.getReservationId(), reservation.getSeat().getSeatId(),
                reservation.getReservationStatus(), reservation.getReservationExpireDt()
        );

        return reservationInfo;
    }

}
