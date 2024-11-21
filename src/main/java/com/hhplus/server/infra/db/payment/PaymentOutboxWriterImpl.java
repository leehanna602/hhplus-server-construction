package com.hhplus.server.infra.db.payment;

import com.hhplus.server.domain.payment.PaymentOutboxWriter;
import com.hhplus.server.domain.payment.model.PaymentOutbox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentOutboxWriterImpl implements PaymentOutboxWriter {

    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    @Override
    public PaymentOutbox save(PaymentOutbox paymentOutbox) {
        return paymentOutboxJpaRepository.save(paymentOutbox);
    }

}
