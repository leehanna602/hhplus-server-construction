package com.hhplus.server.interfaces.v1.payment.res;

import com.hhplus.server.domain.payment.model.PaymentStatus;

public record PaymentRes(
        Long userId,
        PaymentStatus status
) {
}
