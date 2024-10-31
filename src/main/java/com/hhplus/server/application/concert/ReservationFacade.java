package com.hhplus.server.application.concert;

import com.hhplus.server.domain.concert.ConcertReservationDistributedLockService;
import com.hhplus.server.domain.concert.ConcertReservationPessimisticLockService;
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
    private final ReservationService reservationService;
    private final ConcertReservationPessimisticLockService reservationPessimisticLockService;
    private final ConcertReservationDistributedLockService reservationDistributedLockService;

    /* 비관적락 */
    @Transactional
    public ReservationInfo concertSeatReservationWithPessimisticLock(ReservationReq reservationReq) {
        User user = userService.getUser(reservationReq.userId());

        // 예약
        ConcertSeat concertSeatForReservation = reservationPessimisticLockService.findConcertSeatForReservationWithPessimisticLock(
                reservationReq.concertId(), reservationReq.scheduleId(), reservationReq.seatId());

        return reservationService.concertSeatTemporalReservation(user, concertSeatForReservation);
    }

    /* 분산락과 낙관적락 */
    @Transactional
    public ReservationInfo findConcertSeatForReservationWithDistributedLock(ReservationReq reservationReq) {
        User user = userService.getUser(reservationReq.userId());

        // 예약
        ConcertSeat concertSeatForReservation = reservationDistributedLockService.findConcertSeatForReservationWithDistributedLock(
                reservationReq.concertId(), reservationReq.scheduleId(), reservationReq.seatId());

        return reservationService.concertSeatTemporalReservation(user, concertSeatForReservation);
    }




}
