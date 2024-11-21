package com.hhplus.server.domain.payment;

import com.hhplus.server.application.payment.PaymentFacade;
import com.hhplus.server.domain.concert.ConcertService;
import com.hhplus.server.domain.concert.model.*;
import com.hhplus.server.domain.payment.applicationEvent.PaymentEventListener;
import com.hhplus.server.domain.payment.applicationEvent.PaymentEventPublisher;
import com.hhplus.server.domain.payment.dto.PaymentInfo;
import com.hhplus.server.domain.payment.model.PaymentStatus;
import com.hhplus.server.domain.payment.model.Point;
import com.hhplus.server.domain.user.UserService;
import com.hhplus.server.domain.user.model.User;
import com.hhplus.server.infra.db.concert.ReservationJpaRepository;
import com.hhplus.server.infra.kafka.payment.PaymentKafkaProducer;
import com.hhplus.server.interfaces.kafka.payment.PaymentKafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PaymentServiceIntegrationTest {

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private UserService userService;

    private User testUser;
    private Point testPoint;
    private Concert concert;
    private ConcertSchedule concertSchedule;
    private ConcertSeat concertSeat;
    private Reservation reservation;

    private static final int USER_DEFAULT_AMOUNT = 100000;

    @Autowired
    private PointService pointService;
    @Autowired
    private ConcertService concertService;
    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @SpyBean
    private PaymentEventPublisher paymentEventPublisher;

    @SpyBean
    private PaymentEventListener paymentEventListener;

    @SpyBean
    private PaymentKafkaProducer paymentKafkaProducer;

    @SpyBean
    private PaymentKafkaConsumer paymentKafkaConsumer;


    @BeforeEach
    void setUp() {
        // 테스트 사용자 생성
        testUser = User.builder()
                .userId(1L)
                .userName("Test User")
                .build();
        testUser = userService.save(testUser);
        testPoint = Point.builder()
                .pointId(1L)
                .user(testUser)
                .point(USER_DEFAULT_AMOUNT)
                .version(0L)
                .build();
        testPoint = pointService.save(testPoint);

        // 콘서트 생성
        concert = Concert.builder()
                .concertId(1L)
                .concertName("concert1")
                .build();
        concert = concertService.save(concert);

        // 콘서트 일정 생성
        concertSchedule = ConcertSchedule.builder()
                .concertScheduleId(1L)
                .concert(concert)
                .concertDt(LocalDateTime.now().plusDays(2))
                .totalSeat(50)
                .build();
        concertSchedule = concertService.save(concertSchedule);

        // 콘서트 좌석 생성
        concertSeat = ConcertSeat.builder()
                .seatId(1L)
                .concert(concert)
                .concertSchedule(concertSchedule)
                .seatNum(1)
                .price(5000)
                .seatStatus(SeatStatus.AVAILABLE)
                .version(0L)
                .build();
        concertSeat = concertService.save(concertSeat);

        // 예약 생성
        reservation = Reservation.builder()
                .reservationId(1L)
                .user(testUser)
                .seat(concertSeat)
                .reservationDt(LocalDateTime.now())
                .reservationExpireDt(LocalDateTime.now().plusMinutes(5))
                .reservationStatus(ReservationStatus.TEMPORARY)
                .build();
        reservation = reservationJpaRepository.save(reservation);
    }

    @Test
    @DisplayName("사용자가 콘서트 임시예약 건을 결제 요청했을 때 정상적으로 결제 완료와 결제 완료 이벤트가 발행된다.")
    void concurrentPointChargeWithOptimisticLockFailure() throws InterruptedException {
        // given
        String token = UUID.randomUUID().toString();

        // when
        PaymentInfo paymentInfo = paymentFacade.concertPayment(testUser.getUserId(), token, reservation.getReservationId());

        // then
        assertThat(paymentInfo.status()).isEqualTo(PaymentStatus.COMPLETED);

        verify(paymentEventPublisher).successPayment(any(PaymentInfo.class));
        verify(paymentEventListener, times(1)).paymentSuccessHandler(any(PaymentInfo.class));
        verify(paymentKafkaProducer).send(paymentInfo.toString());
        Thread.sleep(3000);
        verify(paymentKafkaConsumer).consume(paymentInfo.toString());
    }


}
