package com.hhplus.server.domain.concert.dto;

import java.util.List;

public record ConcertSeatInfo(
        Long concertId,
        Long scheduleId,
        int totalSeatCnt,
        int availableSeatCnt,
        List<SeatInfo> concertSeatsList
) {

    public record SeatInfo(
            Long seatId,
            int seatNum,
            int price
    ){
    }
}
