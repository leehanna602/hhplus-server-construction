package com.hhplus.server.infra.db.payment;

import com.hhplus.server.domain.base.OutBoxStatus;
import com.hhplus.server.domain.payment.PaymentOutboxReader;
import com.hhplus.server.domain.payment.model.PaymentOutbox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentOutboxReaderImpl implements PaymentOutboxReader {

    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    @Override
    public PaymentOutbox findByPaymentIdWithOptimisticLock(Long paymentId) {
        return paymentOutboxJpaRepository.findByPaymentId(paymentId);
    }

    @Override
    public List<PaymentOutbox> findAllByOutboxStatusAndCreatedAtAfter(OutBoxStatus outBoxStatus, LocalDateTime localDateTime) {
        return paymentOutboxJpaRepository.findAllByOutboxStatusAndCreatedAtAfter(outBoxStatus, localDateTime);
    }
}
