package com.hhplus.server.interfaces.v1.payment.req;

public record PaymentReq(
        Long userId,
        String token,
        Long reservationId
) {
}
