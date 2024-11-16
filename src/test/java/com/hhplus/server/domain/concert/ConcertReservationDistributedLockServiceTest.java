package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.domain.concert.model.*;
import com.hhplus.server.domain.support.exception.CommonException;
import com.hhplus.server.domain.support.exception.ConcertErrorCode;
import com.hhplus.server.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcertReservationDistributedLockServiceTest {

    @Mock
    private ConcertReservationService concertReservationService;

    @InjectMocks
    private ConcertReservationDistributedLockService concertService;

    @Test
    @DisplayName("예약 가능한 콘서트 좌석 예약 요청시 임시 예약 요청 성공")
    void givenAvailableConcertSeat_whenReservationSeat_thenTemporalReservationSuccess() throws InterruptedException {
        // given
        User user = new User(1L, "user1");

        long concertId = 152;
        Concert concert = new Concert(concertId, "concert1");

        long scheduleId = 353;
        ConcertSchedule concertSchedule1 = new ConcertSchedule(scheduleId, concert,
                LocalDateTime.of(2024, 12, 10, 12, 0, 0), 50);

        long concertSeatId = 142L;
        ConcertSeat availableSeat = new ConcertSeat(concertSeatId, concert, concertSchedule1, 1, 15000, SeatStatus.AVAILABLE, 1L);
        ConcertSeat temporaryReservedSeat = new ConcertSeat(concertSeatId, concert, concertSchedule1, 1, 15000, SeatStatus.TEMPORARY_RESERVED, 1L);

        Reservation reservation = new Reservation(user, availableSeat);
        ReservationInfo reservationInfo = new ReservationInfo(null, concertSeatId, ReservationStatus.TEMPORARY, LocalDateTime.now().plusMinutes(10));

        when(concertReservationService.executeInReservationTransaction(user, concertSeatId)).thenReturn(reservationInfo);

        // when
        ReservationInfo reservationInfoResult = concertService.findConcertSeatForReservationWithDistributedLock(user, concertId, scheduleId, temporaryReservedSeat.getSeatId());

        // then
        assertNotNull(reservationInfoResult);
        assertEquals(availableSeat.getSeatId(), reservationInfoResult.seatId());
        assertEquals(temporaryReservedSeat.getSeatStatus(), SeatStatus.TEMPORARY_RESERVED);
        assertEquals(reservationInfoResult.status(), ReservationStatus.TEMPORARY);
    }

    @Test
    @DisplayName("존재하지 않는 좌석 예약 시도 시 CommonException 예외 발생")
    void givenNotExistConcertSeat_whenReservationSeat_thenSeatNotFoundAndThrowException() throws InterruptedException {
        // given
        User user = new User(1L, "user1");
        long concertId = 152;
        long scheduleId = 353;
        long notExistConcertSeatId = 142L;

        when(concertReservationService.executeInReservationTransaction(user, notExistConcertSeatId)).thenThrow(new CommonException(ConcertErrorCode.INVALID_SEAT));

        // when & then
        assertThrows(CommonException.class, () -> concertService.findConcertSeatForReservationWithDistributedLock(user, concertId, scheduleId, notExistConcertSeatId));
    }

}