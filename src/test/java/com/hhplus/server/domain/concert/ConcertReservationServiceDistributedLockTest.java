package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.model.Concert;
import com.hhplus.server.domain.concert.model.ConcertSchedule;
import com.hhplus.server.domain.concert.model.ConcertSeat;
import com.hhplus.server.domain.concert.model.SeatStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcertReservationServiceDistributedLockTest {

    @Mock
    private ConcertReader concertReader;

    @Mock
    private ConcertWriter concertWriter;

    @InjectMocks
    private ConcertReservationDistributedLockService concertService;


    @Test
    void 콘서트좌석_임시예약_성공() {
        // given
        long concertId = 152;
        Concert concert = new Concert(concertId, "concert1");
        long scheduleId = 353;
        ConcertSchedule concertSchedule1 = new ConcertSchedule(scheduleId, concert,
                LocalDateTime.of(2024, 12, 10, 12, 0, 0), 50);
        ConcertSeat concertSeat1 = new ConcertSeat(142L, concert, concertSchedule1, 1, 15000, SeatStatus.AVAILABLE, 1L);
        when(concertWriter.save(any(ConcertSeat.class))).thenReturn(concertSeat1);
        when(concertReader.findConcertSeatForReservationWithPessimisticLock(concertId, scheduleId, concertSeat1.getSeatId())).thenReturn(Optional.of(concertSeat1));

        // when
        ConcertSeat concertSeat = concertService.findConcertSeatForReservationWithDistributedLock(concertId, scheduleId, concertSeat1.getSeatId());

        // then
        assertNotNull(concertSeat);
        assertEquals(concertSeat1.getSeatId(), concertSeat.getSeatId());
        assertEquals(concertSeat.getSeatStatus(), SeatStatus.TEMPORARY_RESERVED);
        verify(concertReader).findConcertSeatForReservationWithPessimisticLock(concertId, scheduleId, concertSeat1.getSeatId());
    }

    @Test
    void 콘서트좌석_임시예약_실패_존재하지않는좌석() {
        // given
        long concertId = 152;
        Concert concert = new Concert(concertId, "concert1");
        long scheduleId = 353;
        ConcertSchedule concertSchedule1 = new ConcertSchedule(scheduleId, concert,
                LocalDateTime.of(2024, 12, 10, 12, 0, 0), 50);
        ConcertSeat concertSeat1 = new ConcertSeat(142L, concert, concertSchedule1, 1, 15000, SeatStatus.AVAILABLE, 1L);
        when(concertReader.findConcertSeatForReservationWithPessimisticLock(concertId, scheduleId, concertSeat1.getSeatId())).thenReturn(Optional.of(concertSeat1));

        // when & then
        assertThrows(RuntimeException.class, () -> concertService.findConcertSeatForReservationWithDistributedLock(concertId, scheduleId, concertSeat1.getSeatId()+1));
    }

}