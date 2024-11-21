package com.hhplus.server.domain.payment.dto;

import com.hhplus.server.domain.payment.model.PaymentStatus;

public record PaymentInfo(
        long paymentId,
        long userId,
        PaymentStatus status
) {
}
