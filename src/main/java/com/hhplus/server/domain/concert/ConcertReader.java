package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.model.Concert;
import com.hhplus.server.domain.concert.model.ConcertSchedule;
import com.hhplus.server.domain.concert.model.ConcertSeat;
import com.hhplus.server.domain.concert.model.SeatStatus;

import java.util.List;
import java.util.Optional;

public interface ConcertReader {
    List<ConcertSchedule> getConcertSchedules(Concert concert);

    ConcertSchedule getConcertSchedules(Long scheduleId);

    Concert getConcert(long concertId);

    List<ConcertSeat> getConcertSeatsBySeatStatus(ConcertSchedule concertSchedule, SeatStatus seatStatus);

    Optional<ConcertSeat> findConcertSeatForReservationWithLock(Long concertId, Long scheduleId, Long seatId);
}
