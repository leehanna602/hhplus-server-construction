package com.hhplus.server.infra.db.payment;

import com.hhplus.server.domain.payment.PaymentOutboxReader;
import com.hhplus.server.domain.payment.model.PaymentOutbox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentOutboxReaderImpl implements PaymentOutboxReader {

    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    @Override
    public PaymentOutbox findByPaymentIdWithOptimisticLock(Long paymentId) {
        return paymentOutboxJpaRepository.findByPaymentId(paymentId);
    }
}
