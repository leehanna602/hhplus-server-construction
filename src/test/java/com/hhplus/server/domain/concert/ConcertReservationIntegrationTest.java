package com.hhplus.server.domain.concert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hhplus.server.application.concert.ReservationFacade;
import com.hhplus.server.domain.concert.applicationEvent.ReservationEventListener;
import com.hhplus.server.domain.concert.applicationEvent.ReservationEventPublisher;
import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.domain.concert.model.*;
import com.hhplus.server.domain.user.UserService;
import com.hhplus.server.domain.user.model.User;
import com.hhplus.server.infra.kafka.concert.ReservationKafkaProducer;
import com.hhplus.server.interfaces.kafka.concert.ReservationKafkaConsumer;
import com.hhplus.server.interfaces.v1.concert.req.ReservationReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ConcertReservationIntegrationTest {

    @Autowired
    private ReservationFacade reservationFacade;
    @Autowired
    private ConcertWriter concertWriter;
    @Autowired
    private UserService userService;

    @SpyBean
    private ReservationOutboxWriter reservationOutboxWriter;
    @SpyBean
    private ReservationEventPublisher reservationEventPublisher;
    @SpyBean
    private ReservationEventListener reservationEventListener;
    @SpyBean
    private ReservationKafkaProducer reservationKafkaProducer;
    @SpyBean
    private ReservationKafkaConsumer reservationKafkaConsumer;

    private User user;
    private Concert concert;
    private ConcertSchedule concertSchedule;
    private ConcertSeat concertSeat;

    @BeforeEach
    void setUp() {
        // User 생성
        user = new User(1L, "user1");
        userService.save(user);

        // 콘서트 생성
        concert = Concert.builder()
                .concertId(1L)
                .concertName("concert1")
                .build();
        concertWriter.save(concert);

        // 콘서트 일정 생성
        concertSchedule = ConcertSchedule.builder()
                .concertScheduleId(1L)
                .concert(concert)
                .concertDt(LocalDateTime.now().plusDays(2))
                .totalSeat(50)
                .build();
        concertWriter.save(concertSchedule);

        // 콘서트 좌석 생성
        concertSeat = ConcertSeat.builder()
                .seatId(1L)
                .concert(concert)
                .concertSchedule(concertSchedule)
                .seatNum(1)
                .price(5000)
                .seatStatus(SeatStatus.AVAILABLE)
                .version(1L)
                .build();
        concertWriter.save(concertSeat);
    }

    @Test
    @DisplayName("콘서트 좌석 예약 요청시 성공 후 이벤트 발행")
    void givenUserRequestConcertSeat_whenConcertReservation_thenSuccessAndPublishEvent() throws InterruptedException, JsonProcessingException {
        // given
        ReservationReq request = new ReservationReq(
                user.getUserId(),
                concert.getConcertId(),
                concertSchedule.getConcertScheduleId(),
                concertSeat.getSeatId()
        );

        // when
        ReservationInfo reservationInfo = reservationFacade.findConcertSeatForReservationWithDistributedLock(request);

        // then
        assertThat(reservationInfo).isNotNull();
        assertThat(reservationInfo.seatId()).isEqualTo(concertSeat.getSeatId());
        assertThat(reservationInfo.status()).isEqualTo(ReservationStatus.TEMPORARY);

        verify(reservationEventPublisher).successReservation(any(ReservationInfo.class));
        verify(reservationEventListener, times(1)).reservationSuccessHandler(any(ReservationInfo.class));
        verify(reservationOutboxWriter).save(any(ReservationOutbox.class));
        verify(reservationKafkaProducer).send(reservationInfo);
        Thread.sleep(3000);
        verify(reservationKafkaConsumer).consume(reservationInfo);
    }

}
