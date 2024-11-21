package com.hhplus.server.domain.concert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.server.domain.concert.applicationEvent.ReservationEventPublisher;
import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.domain.concert.model.*;
import com.hhplus.server.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcertReservationServiceTest {

    @Mock
    private ConcertReader concertReader;

    @Mock
    private ConcertWriter concertWriter;

    @Mock
    private ReservationWriter reservationWriter;

    @Mock
    private ReservationOutboxWriter reservationOutboxWriter;

    @Mock
    private ReservationEventPublisher reservationEventPublisher;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ConcertReservationService concertReservationService;

    @Test
    @DisplayName("콘서트 예약 트랜잭션 요청시 예약 완료 후 완료 이벤트 발행한다")
    void givenUserAndSeatId_whenExecuteInReservationTransaction_thenSuccessReservationAndPublishEvent() {
        // given
        User user = new User(1L, "user1");

        long concertId = 152;
        Concert concert = new Concert(concertId, "concert1");

        long scheduleId = 353;
        ConcertSchedule concertSchedule1 = new ConcertSchedule(scheduleId, concert,
                LocalDateTime.of(2024, 12, 10, 12, 0, 0), 50);

        long concertSeatId = 142L;
        ConcertSeat availableSeat = new ConcertSeat(concertSeatId, concert, concertSchedule1, 1, 15000, SeatStatus.AVAILABLE, 1L);
        ConcertSeat tempReservedSeat = new ConcertSeat(concertSeatId, concert, concertSchedule1, 1, 15000, SeatStatus.TEMPORARY_RESERVED, 1L);

        Reservation afterReservation = new Reservation(1L, user, tempReservedSeat, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5L), ReservationStatus.TEMPORARY);
        ReservationInfo reservationInfo = new ReservationInfo(afterReservation.getReservationId(), concertSeatId, ReservationStatus.TEMPORARY, LocalDateTime.now().plusMinutes(10));

        when(concertReader.findConcertSeatForReservationWithOptimisticLock(concertSeatId)).thenReturn(Optional.of(availableSeat));

        when(concertWriter.save(availableSeat)).thenReturn(tempReservedSeat);
        when(reservationWriter.save(Mockito.any(Reservation.class))).thenReturn(afterReservation);
        when(reservationOutboxWriter.save(Mockito.any(ReservationOutbox.class))).thenReturn(null);

        // when
        ReservationInfo reservationInfoResult = concertReservationService.executeInReservationTransaction(user, concertSeatId);

        // then
        assertNotNull(reservationInfoResult);
        verify(reservationEventPublisher).successReservation(Mockito.any(ReservationInfo.class));

    }
}