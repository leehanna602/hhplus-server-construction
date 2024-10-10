package com.hhplus.server.interfaces.dto.res;

import com.hhplus.server.domain.payment.PaymentStatus;

public record PaymentRes(
        Long userId,
        PaymentStatus status
) {
}
