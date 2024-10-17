package com.hhplus.server.application.concert;

import com.hhplus.server.domain.concert.ConcertService;
import com.hhplus.server.domain.concert.ReservationService;
import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.domain.concert.model.ConcertSeat;
import com.hhplus.server.domain.user.UserService;
import com.hhplus.server.domain.user.model.User;
import com.hhplus.server.domain.waitingQueue.WaitingQueueService;
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
    private final WaitingQueueService waitingQueueService;

    @Transactional
    public ReservationInfo concertSeatReservation(ReservationReq reservationReq) {
        // 토큰 상태 검증
        boolean validateActiveToken = waitingQueueService.validateActiveToken(reservationReq.token());
        if (!validateActiveToken) {
            throw new RuntimeException("Invalid active token");
        }

        User user = userService.getUser(reservationReq.userId());

        // 예약
        ConcertSeat concertSeatForReservation = concertService.findConcertSeatForReservation(
                reservationReq.concertId(), reservationReq.scheduleId(), reservationReq.seatId());

        return reservationService.concertSeatTemporalReservation(user, concertSeatForReservation);
    }

}
