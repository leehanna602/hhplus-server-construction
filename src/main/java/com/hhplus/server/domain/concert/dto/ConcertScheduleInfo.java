package com.hhplus.server.domain.concert.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ConcertScheduleInfo(
        Long concertId,
        List<ConcertSchedules> concertSchedulesList
) {

    public record ConcertSchedules(
            Long concertScheduleId,
            LocalDateTime concertDt,
            int totalSeat
    ){
    }
}
