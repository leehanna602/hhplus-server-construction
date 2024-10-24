package com.hhplus.server.application.concert.scheduling;

import com.hhplus.server.domain.concert.ConcertService;
import com.hhplus.server.domain.concert.ReservationService;
import com.hhplus.server.domain.concert.model.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationService reservationService;
    private final ConcertService concertService;

    /* 결제 시간 만료
     * - ReservationStatus TEMPORARY -> EXPIRED 상태 변경
     * - SeatStatus TEMPORARY_RESERVED -> AVAILABLE */
    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void expiredSeatReservation() {
        List<Reservation> reservations = reservationService.getReservationsTemporaryToExpired();
        for (Reservation reservation : reservations) {
            reservationService.reservationToExpired(reservation);
            concertService.concertSeatToExpired(reservation.getSeat());
        }
    }

}
