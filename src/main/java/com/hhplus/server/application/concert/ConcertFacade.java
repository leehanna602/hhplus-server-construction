package com.hhplus.server.application.concert;

import com.hhplus.server.domain.concert.ConcertService;
import com.hhplus.server.domain.concert.dto.ConcertScheduleInfo;
import com.hhplus.server.domain.concert.dto.ConcertSeatInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class ConcertFacade {

    private final ConcertService concertService;

    public ConcertScheduleInfo getConcertSchedule(long concertId) {
        return concertService.getConcertSchedule(concertId);
    }

    public ConcertSeatInfo getConcertSeats(long concertId, long scheduleId) {
        return concertService.getConcertSeats(concertId, scheduleId);
    }

}
