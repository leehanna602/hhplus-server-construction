package com.hhplus.server.domain.payment.applicationEvent;

import com.hhplus.server.domain.payment.dto.PaymentInfo;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public PaymentEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void successPayment(PaymentInfo paymentInfo) {
        applicationEventPublisher.publishEvent(paymentInfo);
    }

}
