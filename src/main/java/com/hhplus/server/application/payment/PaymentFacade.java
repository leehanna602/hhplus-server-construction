package com.hhplus.server.application.payment;

import com.hhplus.server.domain.support.exception.CommonException;
import com.hhplus.server.domain.common.exception.WaitingQueueErrorCode;
import com.hhplus.server.domain.concert.ConcertService;
import com.hhplus.server.domain.concert.ReservationService;
import com.hhplus.server.domain.concert.model.Reservation;
import com.hhplus.server.domain.payment.PaymentService;
import com.hhplus.server.domain.payment.PointService;
import com.hhplus.server.domain.payment.dto.PaymentInfo;
import com.hhplus.server.domain.payment.model.PaymentStatus;
import com.hhplus.server.domain.payment.model.Point;
import com.hhplus.server.domain.payment.model.TransactionType;
import com.hhplus.server.domain.user.UserService;
import com.hhplus.server.domain.user.model.User;
import com.hhplus.server.domain.waitingQueue.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentService paymentService;
    private final ReservationService reservationService;
    private final PointService pointService;
    private final ConcertService concertService;
    private final WaitingQueueService waitingQueueService;
    private final UserService userService;

    @Transactional
    public PaymentInfo concertPayment(Long userId, String token, Long reservationId) {
        // 토큰 상태 검증
        boolean validateActiveToken = waitingQueueService.validateActiveToken(token);
        if (!validateActiveToken) {
            throw new CommonException(WaitingQueueErrorCode.INVALID_TOKEN);
        }

        User user = userService.getUser(userId);

        // 결제정보 확인
        Reservation reservation = reservationService.validateReservationId(reservationId);

        // 포인트 차감 및 히스토리, 결제 내역 생성
        Point point = pointService.pointTransaction(userId, reservation.getSeat().getPrice(), TransactionType.USE);
        paymentService.completePayment(user, reservation, point);

        // 좌석 상태와 예약 상태 완료 처리
        reservation = reservationService.completeStatus(reservation);
        concertService.completeStatus(reservation.getSeat());

        // 토큰 만료
        waitingQueueService.expiredToken(token);

        return new PaymentInfo(userId, PaymentStatus.COMPLETED);
    }

}
