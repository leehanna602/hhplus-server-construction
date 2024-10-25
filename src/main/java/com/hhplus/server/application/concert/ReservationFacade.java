package com.hhplus.server.application.concert;

import com.hhplus.server.domain.concert.ConcertService;
import com.hhplus.server.domain.concert.ReservationService;
import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.domain.concert.model.ConcertSeat;
import com.hhplus.server.domain.user.UserService;
import com.hhplus.server.domain.user.model.User;
import com.hhplus.server.interfaces.v1.concert.req.ReservationReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class ReservationFacade {
    private final UserService userService;
    private final ConcertService concertService;
    private final ReservationService reservationService;

    @Transactional
    public ReservationInfo concertSeatReservation(ReservationReq reservationReq) {
        User user = userService.getUser(reservationReq.userId());

        // 예약
        ConcertSeat concertSeatForReservation = concertService.findConcertSeatForReservation(
                reservationReq.concertId(), reservationReq.scheduleId(), reservationReq.seatId());

        return reservationService.concertSeatTemporalReservation(user, concertSeatForReservation);
    }

}
