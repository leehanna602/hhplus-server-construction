package com.hhplus.server.domain.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.server.domain.concert.ConcertService;
import com.hhplus.server.domain.concert.ReservationService;
import com.hhplus.server.domain.concert.model.*;
import com.hhplus.server.domain.payment.applicationEvent.PaymentEventPublisher;
import com.hhplus.server.domain.payment.dto.PaymentInfo;
import com.hhplus.server.domain.payment.model.*;
import com.hhplus.server.domain.user.model.User;
import com.hhplus.server.domain.waitingQueue.WaitingQueueService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentWriter paymentWriter;

    @Mock
    private ReservationService reservationService;

    @Mock
    private PointService pointService;

    @Mock
    private ConcertService concertService;

    @Mock
    private WaitingQueueService waitingQueueService;

    @Mock
    private PaymentEventPublisher paymentEventPublisher;

    @Mock
    private PaymentOutboxWriter paymentOutboxWriter;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    @DisplayName("정상적인 콘서트 예약 건을 결제 요청시 정상 결제 완료 후 완료 이벤트 발행한다.")
    void givenExistReservationId_whenPaymentTransaction_thenSuccessAndPublishEvent() {
        // given
        User user = new User(1L, "user1");
        String token = UUID.randomUUID().toString();

        long concertId = 152;
        Concert concert = new Concert(concertId, "concert1");

        long scheduleId = 353;
        ConcertSchedule concertSchedule1 = new ConcertSchedule(scheduleId, concert,
                LocalDateTime.of(2024, 12, 10, 12, 0, 0), 50);

        long concertSeatId = 142L;
        ConcertSeat tempReservedSeat = new ConcertSeat(concertSeatId, concert, concertSchedule1, 1, 15000, SeatStatus.TEMPORARY_RESERVED, 1L);

        long reservationId = 1L;
        Reservation reservation = new Reservation(reservationId, user, tempReservedSeat, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5L), ReservationStatus.TEMPORARY);

        Point point = new Point(1L, user, 100000, 1L);

        Payment payment = new Payment(1L, user, reservation, reservation.getSeat().getPrice(), PaymentStatus.COMPLETED, LocalDateTime.now());
        PaymentInfo paymentInfo = new PaymentInfo(1L, user.getUserId(), PaymentStatus.COMPLETED);
        String paymentInfoJson = "{\"paymentId\":1,\"userId\":1,\"status\":\"COMPLETED\"}";

        when(reservationService.validateReservationId(reservationId)).thenReturn(reservation);
        when(pointService.pointUseTransaction(user.getUserId(), reservation.getSeat().getPrice(), TransactionType.USE)).thenReturn(point);
        when(paymentWriter.save(any(Payment.class))).thenReturn(payment);
        when(reservationService.completeStatus(reservation)).thenReturn(reservation);

        // when
        PaymentInfo paymentResult = paymentService.payment(user, token, reservationId);

        // then
        assertNotNull(paymentResult);
        assertEquals(1L, paymentResult.paymentId());
        assertEquals(PaymentStatus.COMPLETED, paymentResult.status());

        verify(concertService).completeStatus(reservation.getSeat());
        verify(paymentWriter).save(any(Payment.class));
        verify(paymentOutboxWriter).save(any(PaymentOutbox.class));
        verify(paymentEventPublisher).successPayment(any(PaymentInfo.class));
        verify(waitingQueueService).removeActiveToken(token);
    }

    @Test
    @DisplayName("결제 완료시 예약의 상태를 COMPLETED로 바꾼다.")
    void givenCompletePayment_whenUpdateCompleteReservation_thenSuccessSaveComplete() {
        // given
        User user = new User(1L, "user1");

        long concertId = 152;
        Concert concert = new Concert(concertId, "concert1");

        long scheduleId = 353;
        ConcertSchedule concertSchedule1 = new ConcertSchedule(scheduleId, concert,
                LocalDateTime.of(2024, 12, 10, 12, 0, 0), 50);

        long concertSeatId = 142L;
        ConcertSeat tempReservedSeat = new ConcertSeat(concertSeatId, concert, concertSchedule1, 1, 15000, SeatStatus.TEMPORARY_RESERVED, 1L);

        Reservation reservation = new Reservation(1L, user, tempReservedSeat, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5L), ReservationStatus.TEMPORARY);
        Payment payment = new Payment(user, reservation, 5000, PaymentStatus.COMPLETED);
        when(paymentWriter.save(any(Payment.class))).thenReturn(payment);

        // when
        Payment paymentResult = paymentService.completePayment(user, reservation);

        // then
        assertNotNull(paymentResult);
        assertEquals(paymentResult.getPaymentStatus(), PaymentStatus.COMPLETED);
        verify(paymentWriter).save(any(Payment.class));
    }
}