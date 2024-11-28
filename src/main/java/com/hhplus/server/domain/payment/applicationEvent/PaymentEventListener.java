package com.hhplus.server.domain.payment.applicationEvent;

import com.hhplus.server.domain.payment.PaymentOutboxService;
import com.hhplus.server.domain.payment.dto.PaymentInfo;
import com.hhplus.server.infra.kafka.payment.PaymentKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventListener {

    private final PaymentOutboxService paymentOutboxService;
    private final PaymentKafkaProducer paymentKafkaProducer;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void paymentSuccessHandler(PaymentInfo paymentInfo) {
        paymentOutboxService.initPaymentOutbox(paymentInfo);
        paymentKafkaProducer.send(paymentInfo);
    }

}
