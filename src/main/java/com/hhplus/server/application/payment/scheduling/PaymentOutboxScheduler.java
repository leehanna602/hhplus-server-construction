package com.hhplus.server.application.payment.scheduling;

import com.hhplus.server.domain.payment.PaymentOutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentOutboxScheduler {

    private final PaymentOutboxService paymentOutboxService;

    @Scheduled(fixedRate = 5000)
    public void successReservationEventPublish() {
        paymentOutboxService.paymentEventPublish();
    }


}
