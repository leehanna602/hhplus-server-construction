package com.hhplus.server.application.payment;

import com.hhplus.server.domain.concert.ConcertService;
import com.hhplus.server.domain.concert.ReservationService;
import com.hhplus.server.domain.concert.model.Reservation;
import com.hhplus.server.domain.payment.PointService;
import com.hhplus.server.domain.payment.dto.PaymentInfo;
import com.hhplus.server.domain.payment.model.PaymentStatus;
import com.hhplus.server.domain.payment.model.Point;
import com.hhplus.server.domain.payment.model.TransactionType;
import com.hhplus.server.domain.waitingQueue.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final ReservationService reservationService;
    private final PointService pointService;
    private final ConcertService concertService;
    private final WaitingQueueService waitingQueueService;

    @Transactional
    public PaymentInfo concertPayment(Long userId, String token, Long reservationId) {
        // 토큰 상태 검증
        boolean validateActiveToken = waitingQueueService.validateActiveToken(token);
        if (!validateActiveToken) {
            throw new RuntimeException("Invalid active token");
        }

        // 결제정보 확인
        Reservation reservation = reservationService.validateReservationId(reservationId);

        // 포인트 차감 및 히스토리 생성
        Point point = pointService.pointTransaction(userId, reservation.getSeat().getPrice(), TransactionType.USE);

        // 좌석 상태와 예약 상태 완료 처리
        reservation = reservationService.completeStatus(reservation);
        concertService.completeStatus(reservation.getSeat());

        // 토큰 만료
        waitingQueueService.expiredToken(token);

        return new PaymentInfo(userId, PaymentStatus.COMPLETED);
    }

}
