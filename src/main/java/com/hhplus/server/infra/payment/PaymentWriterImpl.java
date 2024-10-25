package com.hhplus.server.infra.payment;

import com.hhplus.server.domain.payment.PaymentWriter;
import com.hhplus.server.domain.payment.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentWriterImpl implements PaymentWriter {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

}
