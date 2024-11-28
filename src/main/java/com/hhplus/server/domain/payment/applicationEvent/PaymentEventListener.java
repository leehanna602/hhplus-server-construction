package com.hhplus.server.domain.payment.applicationEvent;

import com.hhplus.server.domain.payment.dto.PaymentInfo;
import com.hhplus.server.infra.kafka.payment.PaymentKafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class PaymentEventListener {

    private final PaymentKafkaProducer paymentKafkaProducer;

    public PaymentEventListener(PaymentKafkaProducer paymentKafkaProducer) {
        this.paymentKafkaProducer = paymentKafkaProducer;
    }

//    @Async
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void paymentSuccessHandler(PaymentInfo paymentInfo) throws InterruptedException {
//        log.info("PaymentEventListener paymentSuccessHandler start: {}", paymentInfo);
//        Thread.sleep(3000);
//        log.info("PaymentEventListener paymentSuccessHandler end: {}", paymentInfo);
//    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void paymentSuccessHandler(PaymentInfo paymentInfo) {
        paymentKafkaProducer.send(paymentInfo.toString());
    }

}
