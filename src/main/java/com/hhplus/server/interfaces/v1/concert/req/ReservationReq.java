package com.hhplus.server.interfaces.v1.concert.req;

public record ReservationReq(
        Long userId,
        Long concertId,
        Long scheduleId,
        Long seatId
) {
}
