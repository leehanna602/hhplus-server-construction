package com.hhplus.server.domain.payment;

import com.hhplus.server.domain.payment.model.Payment;

public interface PaymentWriter {
    Payment save(Payment payment);
}
