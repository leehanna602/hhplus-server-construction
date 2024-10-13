package com.hhplus.server.interfaces.dto.req;

public record ReservationReq(
        Long userId,
        String token,
        Long concertId,
        Long scheduleId,
        Long seatId
) {
}
