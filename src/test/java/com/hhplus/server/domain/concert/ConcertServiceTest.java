package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.dto.ConcertScheduleInfo;
import com.hhplus.server.domain.concert.dto.ConcertSeatInfo;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertReader concertReader;

    @Mock
    private ConcertWriter concertWriter;

    @InjectMocks
    private ConcertService concertService;

    @Test
    void 존재하는콘서트아이디_콘서트스케줄_조회_성공() {
        // given
        long concertId = 1;
        Concert concert = new Concert(concertId, "concert1");
        when(concertReader.getConcert(concertId)).thenReturn(concert);

        ConcertSchedule concertSchedule1 = new ConcertSchedule(1L, concert,
                LocalDateTime.of(2024, 12, 10, 12, 0, 0), 50);
        ConcertSchedule concertSchedule2 = new ConcertSchedule(2L, concert,
                LocalDateTime.of(2024, 12, 10, 14, 0, 0), 50);
        ConcertSchedule concertSchedule3 = new ConcertSchedule(3L, concert,
                LocalDateTime.of(2024, 12, 11, 12, 0, 0), 50);
        List<ConcertSchedule> concertSchedules = Arrays.asList(concertSchedule1, concertSchedule2, concertSchedule3);
        when(concertReader.getConcertSchedules(concert)).thenReturn(concertSchedules);

        //when
        ConcertScheduleInfo concertScheduleInfo = concertService.getConcertSchedule(concertId);

        //then
        assertNotNull(concertScheduleInfo);
        assertEquals(concertScheduleInfo.concertSchedulesList().size(), 3);
        verify(concertReader).getConcert(concertId);
        verify(concertReader).getConcertSchedules(concert);
    }

    @Test
    void 예약가능한_콘서트좌석_존재할때_조회_성공() {
        // given
        long concertId = 152;
        Concert concert = new Concert(concertId, "concert1");
        long scheduleId = 353;
        ConcertSchedule concertSchedule1 = new ConcertSchedule(scheduleId, concert,
                LocalDateTime.of(2024, 12, 10, 12, 0, 0), 50);
        when(concertReader.getConcertSchedules(scheduleId)).thenReturn(concertSchedule1);
        ConcertSeat concertSeat1 = new ConcertSeat(142L, concert, concertSchedule1, 1, 15000, SeatStatus.AVAILABLE);
        ConcertSeat concertSeat2 = new ConcertSeat(145L, concert, concertSchedule1, 3, 15000, SeatStatus.AVAILABLE);
        List<ConcertSeat> concertSeatList = Arrays.asList(concertSeat1, concertSeat2);
        when(concertReader.getConcertSeatsBySeatStatus(concertSchedule1, SeatStatus.AVAILABLE)).thenReturn(concertSeatList);

        // when
        ConcertSeatInfo concertSeats = concertService.getConcertSeats(concertId, scheduleId);

        // then
        assertNotNull(concertSeat1);
        assertEquals(concertSeats.concertId(), concertId);
        assertEquals(concertSeats.availableSeatCnt(), concertSeatList.size());
        verify(concertReader).getConcertSchedules(scheduleId);
        verify(concertReader).getConcertSeatsBySeatStatus(concertSchedule1, SeatStatus.AVAILABLE);
    }

    @Test
    void 예약가능한_콘서트좌석_없음_조회_성공() {
        // given
        long concertId = 152;
        Concert concert = new Concert(concertId, "concert1");
        long scheduleId = 353;
        ConcertSchedule concertSchedule1 = new ConcertSchedule(scheduleId, concert,
                LocalDateTime.of(2024, 12, 10, 12, 0, 0), 50);
        when(concertReader.getConcertSchedules(scheduleId)).thenReturn(concertSchedule1);
        List<ConcertSeat> concertSeatList = new ArrayList<>();
        when(concertReader.getConcertSeatsBySeatStatus(concertSchedule1, SeatStatus.AVAILABLE)).thenReturn(concertSeatList);

        // when
        ConcertSeatInfo concertSeats = concertService.getConcertSeats(concertId, scheduleId);

        // then
        assertEquals(concertSeats.concertId(), concertId);
        assertEquals(concertSeats.availableSeatCnt(), concertSeatList.size());
        assertEquals(concertSeats.availableSeatCnt(), 0);
        verify(concertReader).getConcertSchedules(scheduleId);
        verify(concertReader).getConcertSeatsBySeatStatus(concertSchedule1, SeatStatus.AVAILABLE);
    }

    @Test
    void 콘서트좌석_임시예약_성공() {
        // given
        long concertId = 152;
        Concert concert = new Concert(concertId, "concert1");
        long scheduleId = 353;
        ConcertSchedule concertSchedule1 = new ConcertSchedule(scheduleId, concert,
                LocalDateTime.of(2024, 12, 10, 12, 0, 0), 50);
        ConcertSeat concertSeat1 = new ConcertSeat(142L, concert, concertSchedule1, 1, 15000, SeatStatus.AVAILABLE);
        when(concertWriter.save(any(ConcertSeat.class))).thenReturn(concertSeat1);
        when(concertReader.findConcertSeatForReservationWithLock(concertId, scheduleId, concertSeat1.getSeatId())).thenReturn(Optional.of(concertSeat1));

        // when
        ConcertSeat concertSeat = concertService.findConcertSeatForReservation(concertId, scheduleId, concertSeat1.getSeatId());

        // then
        assertNotNull(concertSeat);
        assertEquals(concertSeat1.getSeatId(), concertSeat.getSeatId());
        assertEquals(concertSeat.getSeatStatus(), SeatStatus.TEMPORARY_RESERVED);
        verify(concertReader).findConcertSeatForReservationWithLock(concertId, scheduleId, concertSeat1.getSeatId());
    }

    @Test
    void 콘서트좌석_임시예약_실패_존재하지않는좌석() {
        // given
        long concertId = 152;
        Concert concert = new Concert(concertId, "concert1");
        long scheduleId = 353;
        ConcertSchedule concertSchedule1 = new ConcertSchedule(scheduleId, concert,
                LocalDateTime.of(2024, 12, 10, 12, 0, 0), 50);
        ConcertSeat concertSeat1 = new ConcertSeat(142L, concert, concertSchedule1, 1, 15000, SeatStatus.AVAILABLE);
        when(concertReader.findConcertSeatForReservationWithLock(concertId, scheduleId, concertSeat1.getSeatId())).thenReturn(Optional.of(concertSeat1));

        // when & then
        assertThrows(RuntimeException.class, () -> concertService.findConcertSeatForReservation(concertId, scheduleId, concertSeat1.getSeatId()+1));
    }

}