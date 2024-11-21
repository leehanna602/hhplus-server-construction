package com.hhplus.server.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.server.domain.base.OutBoxStatus;
import com.hhplus.server.domain.payment.applicationEvent.PaymentEventPublisher;
import com.hhplus.server.domain.payment.dto.PaymentInfo;
import com.hhplus.server.domain.payment.model.PaymentOutbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class PaymentOutboxService {

    private final PaymentOutboxReader paymentOutboxReader;
    private final PaymentOutboxWriter paymentOutboxWriter;
    private final PaymentEventPublisher paymentEventPublisher;
    private final ObjectMapper objectMapper;

    @Transactional
    public void updateStatusPublish(PaymentInfo message) {
        PaymentOutbox paymentOutbox = paymentOutboxReader.findByPaymentIdWithOptimisticLock(message.paymentId());
        paymentOutbox.outBoxPublish();
        paymentOutboxWriter.save(paymentOutbox);
        log.info("Payment outbox status update publish: {}", paymentOutbox);
    }

    public void paymentEventPublish() {
        List<PaymentOutbox> paymentOutboxList = paymentOutboxReader.findAllByOutboxStatusAndCreatedAtAfter(
                OutBoxStatus.INIT, LocalDateTime.now().minusMinutes(5)
        );
        log.info("update paymentOutboxList size: {}", paymentOutboxList.size());

        for (PaymentOutbox paymentOutbox : paymentOutboxList) {
            try {
                paymentEventPublisher.successPayment(objectMapper.readValue(paymentOutbox.getPayload(), PaymentInfo.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
