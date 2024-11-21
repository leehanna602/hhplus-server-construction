package com.hhplus.server.domain.payment;

import com.hhplus.server.domain.payment.dto.PaymentInfo;
import com.hhplus.server.domain.payment.model.PaymentOutbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
public class PaymentOutboxService {

    private final PaymentOutboxReader paymentOutboxReader;
    private final PaymentOutboxWriter paymentOutboxWriter;

    @Transactional
    public void updateStatusPublish(PaymentInfo message) {
        PaymentOutbox paymentOutbox = paymentOutboxReader.findByPaymentIdWithOptimisticLock(message.paymentId());
        paymentOutbox.outBoxPublish();
        paymentOutboxWriter.save(paymentOutbox);
        log.info("Payment outbox status update publish: {}", paymentOutbox);
    }

}
