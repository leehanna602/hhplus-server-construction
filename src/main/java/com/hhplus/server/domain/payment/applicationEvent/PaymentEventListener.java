package com.hhplus.server.domain.payment.applicationEvent;

import com.hhplus.server.domain.payment.dto.PaymentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class PaymentEventListener {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void paymentSuccessHandler(PaymentInfo paymentInfo) throws InterruptedException {
        log.info("PaymentEventListener paymentSuccessHandler start: {}", paymentInfo);
        Thread.sleep(3000);
        log.info("PaymentEventListener paymentSuccessHandler end: {}", paymentInfo);
    }

}
