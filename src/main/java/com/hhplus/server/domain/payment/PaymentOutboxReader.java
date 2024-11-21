package com.hhplus.server.domain.payment;

import com.hhplus.server.domain.payment.model.PaymentOutbox;

public interface PaymentOutboxReader {

    PaymentOutbox findByPaymentIdWithOptimisticLock(Long paymentId);
}
