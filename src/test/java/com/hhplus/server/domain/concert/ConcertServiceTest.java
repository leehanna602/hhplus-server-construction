package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.dto.ConcertScheduleInfo;
import com.hhplus.server.domain.concert.dto.ConcertSeatInfo;
import com.hhplus.server.domain.concert.model.Concert;
import com.hhplus.server.domain.concert.model.ConcertSchedule;
import com.hhplus.server.domain.concert.model.ConcertSeat;
import com.hhplus.server.domain.concert.model.SeatStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertReader concertReader;

    @InjectMocks
    private ConcertService concertService;

    @Test
    @DisplayName("존재하는 콘서트아이디로 콘서트 스케줄 정보 조회 성공")
    void givenExistConcertId_whenRequestConcertSchedule_thenConcertScheduleInfoResponseSuccess() {
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
    @DisplayName("예약 가능한 콘서트 좌석 존재시 존재하는 콘서트 좌석 조회 성공")
    void givenExistConcertIdAndScheduleId_whenRequestExistConcertSeats_thenConcertSeatListResponseSuccess() {
        // given
        long concertId = 152;
        Concert concert = new Concert(concertId, "concert1");
        long scheduleId = 353;
        ConcertSchedule concertSchedule1 = new ConcertSchedule(scheduleId, concert,
                LocalDateTime.of(2024, 12, 10, 12, 0, 0), 50);
        when(concertReader.getConcertSchedules(scheduleId)).thenReturn(concertSchedule1);
        ConcertSeat concertSeat1 = new ConcertSeat(142L, concert, concertSchedule1, 1, 15000, SeatStatus.AVAILABLE, 1L);
        ConcertSeat concertSeat2 = new ConcertSeat(145L, concert, concertSchedule1, 3, 15000, SeatStatus.AVAILABLE, 1L);
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
    @DisplayName("존재하는 콘서트의 예약 가능한 좌석 조회시 예약 가능한 좌석이 없음")
    void givenExistConcertSchedule_whenRequestAvailableSeats_thenAvailableSeatsListSizeZero() {
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



}