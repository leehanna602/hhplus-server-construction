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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcertReservationDistributedLockServiceTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private ConcertReservationService concertReservationService;

    @Mock
    private RLock lock;

    @InjectMocks
    private ConcertReservationDistributedLockService concertService;

    private static final int waitTime = 5;
    private static final int leaseTime = 10;
    private static final TimeUnit timeUnit = TimeUnit.SECONDS;

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

        when(redissonClient.getLock("reserv:" + concertId + ":" + scheduleId + ":" + concertSeatId)).thenReturn(lock);
        when(lock.getName()).thenReturn("reserv:" + concertId + ":" + scheduleId + ":" + concertSeatId);
        when(lock.tryLock(waitTime, leaseTime, timeUnit)).thenReturn(true);
        when(lock.isLocked()).thenReturn(true);
        when(concertReservationService.executeInReservationTransaction(user, concertSeatId)).thenReturn(reservationInfo);
        when(lock.isHeldByCurrentThread()).thenReturn(true);

        // when
        ReservationInfo reservationInfoResult = concertService.findConcertSeatForReservationWithDistributedLock(user, concertId, scheduleId, temporaryReservedSeat.getSeatId());

        // then
        assertNotNull(reservationInfoResult);
        assertEquals(availableSeat.getSeatId(), reservationInfoResult.seatId());
        assertEquals(temporaryReservedSeat.getSeatStatus(), SeatStatus.TEMPORARY_RESERVED);
        assertEquals(reservationInfoResult.status(), ReservationStatus.TEMPORARY);
        verify(lock).unlock();
    }

    @Test
    @DisplayName("예약 가능한 콘서트 좌석 예약 요청시 분산락 획득 실패로 예외 발생")
    void givenAvailableConcertSeat_whenReservationSeat_thenDistributedLockAcquisitionFailsAndThrowException() throws InterruptedException {
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

        when(redissonClient.getLock("reserv:" + concertId + ":" + scheduleId + ":" + concertSeatId)).thenReturn(lock);
        when(lock.tryLock(waitTime, leaseTime, timeUnit)).thenReturn(false);

        // when & then
        assertThrows(CommonException.class, () ->
                concertService.findConcertSeatForReservationWithDistributedLock(user, concertId, scheduleId, concertSeatId));
    }


    @Test
    @DisplayName("존재하지 않는 좌석 예약 시도 시 CommonException 예외 발생")
    void givenNotExistConcertSeat_whenReservationSeat_thenSeatNotFoundAndThrowException() throws InterruptedException {
        // given
        User user = new User(1L, "user1");
        long concertId = 152;
        long scheduleId = 353;
        long notExistConcertSeatId = 142L;

        when(redissonClient.getLock("reserv:" + concertId + ":" + scheduleId + ":" + notExistConcertSeatId)).thenReturn(lock);
        when(lock.getName()).thenReturn("reserv:" + concertId + ":" + scheduleId + ":" + notExistConcertSeatId);
        when(lock.tryLock(waitTime, leaseTime, timeUnit)).thenReturn(true);
        when(lock.isLocked()).thenReturn(true);
        when(lock.isHeldByCurrentThread()).thenReturn(true);
        when(concertReservationService.executeInReservationTransaction(user, notExistConcertSeatId)).thenThrow(new CommonException(ConcertErrorCode.INVALID_SEAT));

        // when & then
        assertThrows(CommonException.class, () -> concertService.findConcertSeatForReservationWithDistributedLock(user, concertId, scheduleId, notExistConcertSeatId));
        verify(lock).unlock();
    }

    @Test
    @DisplayName("콘서트 좌석 예약 요청 시 락 획득 중 인터럽트 발생 시 예외 발생")
    void givenExistConcertSeat_whenReservationConcertSeat_thenInterruptedDuringLockAcquisitionAndThrowException() throws InterruptedException {
        // given
        User user = new User(1L, "user1");
        long concertId = 152;
        long scheduleId = 353;
        long concertSeatId = 142L;

        when(redissonClient.getLock("reserv:" + concertId + ":" + scheduleId + ":" + concertSeatId)).thenReturn(lock);
        when(lock.tryLock(waitTime, leaseTime, timeUnit)).thenThrow(InterruptedException.class);

        // when & then
        assertThrows(CommonException.class, () -> concertService.findConcertSeatForReservationWithDistributedLock(user, concertId, scheduleId, concertSeatId));
    }

}