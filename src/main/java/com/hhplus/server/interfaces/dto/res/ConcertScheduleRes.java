package com.hhplus.server.interfaces.dto.res;

import java.util.List;

public record ConcertScheduleRes(
        Long concertId,
        List<String> availableDates
) {
}
