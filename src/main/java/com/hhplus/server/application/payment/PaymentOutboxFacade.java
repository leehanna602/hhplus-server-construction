package com.hhplus.server.application.payment;

import com.hhplus.server.domain.payment.PaymentOutboxService;
import com.hhplus.server.domain.payment.dto.PaymentInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class PaymentOutboxFacade {

    private final PaymentOutboxService paymentOutboxService;

    public void publishMessageSuccess(PaymentInfo message) {
        paymentOutboxService.updateStatusPublish(message);
    }
}
