package com.hhplus.server.interfaces.v1.concert.res;

import java.util.List;

public record ConcertSeatsRes(
        Long concertId,
        Long scheduleId,
        int totalSeatCnt,
        int availableSeatCnt,
        List<SeatsRes> availableSeats
) {
    public record SeatsRes(
            Long seatId,
            int seatNum,
            int price
    ){
    }
}
