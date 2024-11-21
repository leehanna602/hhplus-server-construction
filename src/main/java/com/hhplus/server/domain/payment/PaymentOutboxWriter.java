package com.hhplus.server.domain.payment;

import com.hhplus.server.domain.payment.model.PaymentOutbox;

public interface PaymentOutboxWriter {
    PaymentOutbox save(PaymentOutbox paymentOutbox);
}
