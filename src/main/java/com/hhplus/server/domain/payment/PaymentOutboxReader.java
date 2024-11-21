package com.hhplus.server.domain.payment;

import com.hhplus.server.domain.base.OutBoxStatus;
import com.hhplus.server.domain.payment.model.PaymentOutbox;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentOutboxReader {

    PaymentOutbox findByPaymentIdWithOptimisticLock(Long paymentId);

    List<PaymentOutbox> findAllByOutboxStatusAndCreatedAtAfter(OutBoxStatus outBoxStatus, LocalDateTime localDateTime);
}
