package com.hhplus.server.interfaces.dto.res;

import java.util.List;

public record ConcertSeatsRes(
        Long concertId,
        Long scheduleId,
        int availableSeatsCnt,
        List<SeatsRes> availableSeats
) {
}
