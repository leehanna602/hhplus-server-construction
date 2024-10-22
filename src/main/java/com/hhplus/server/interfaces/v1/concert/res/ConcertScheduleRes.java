package com.hhplus.server.interfaces.v1.concert.res;

import java.time.LocalDateTime;
import java.util.List;

public record ConcertScheduleRes(
        Long concertId,
        List<ConcertScheduleData> availableConcertSchedule
) {

    public record ConcertScheduleData(
            Long concertScheduleId,
            LocalDateTime concertDt
    ){
    }

}
