package com.hhplus.server.domain.payment;

import com.hhplus.server.domain.concert.ConcertService;
import com.hhplus.server.domain.concert.ReservationService;
import com.hhplus.server.domain.concert.model.Reservation;
import com.hhplus.server.domain.payment.applicationEvent.PaymentEventPublisher;
import com.hhplus.server.domain.payment.dto.PaymentInfo;
import com.hhplus.server.domain.payment.model.Payment;
import com.hhplus.server.domain.payment.model.PaymentStatus;
import com.hhplus.server.domain.payment.model.Point;
import com.hhplus.server.domain.payment.model.TransactionType;
import com.hhplus.server.domain.user.model.User;
import com.hhplus.server.domain.waitingQueue.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentReader paymentReader;
    private final PaymentWriter paymentWriter;

    private final ReservationService reservationService;
    private final PointService pointService;
    private final ConcertService concertService;
    private final WaitingQueueService waitingQueueService;

    private final PaymentEventPublisher paymentEventPublisher;

    @Transactional
    public PaymentInfo payment(User user, String token, Long reservationId) {

        // 1. 결제정보 확인
        Reservation reservation = reservationService.validateReservationId(reservationId);

        // 2. 포인트 차감 및 히스토리, 결제 내역 생성
        Point point = pointService.pointUseTransaction(user.getUserId(), reservation.getSeat().getPrice(), TransactionType.USE);

        // 3. 결제 완료에 따른 관련 데이터 상태 변경
        Payment payment = completePayment(user, reservation);
        reservation = reservationService.completeStatus(reservation);
        concertService.completeStatus(reservation.getSeat());

        // 4. 결제 완료 이벤트 발행
        PaymentInfo paymentInfo = new PaymentInfo(user.getUserId(), PaymentStatus.COMPLETED);
        paymentEventPublisher.successPayment(paymentInfo);

        // 5. 토큰 만료
        waitingQueueService.removeActiveToken(token);

        return paymentInfo;
    }


    @Transactional
    public Payment completePayment(User user, Reservation reservation) {
        Payment payment = new Payment(user, reservation, reservation.getSeat().getPrice(), PaymentStatus.COMPLETED);
        payment = paymentWriter.save(payment);
        return payment;
    }
}
