package com.hhplus.server.interfaces.dto.req;

public record PaymentReq(
        Long userId,
        String token,
        Long reservationId
) {
}
