package com.hhplus.server.interfaces.kafka.payment;

import com.hhplus.server.application.payment.PaymentOutboxFacade;
import com.hhplus.server.domain.payment.dto.PaymentInfo;
import com.hhplus.server.interfaces.kafka.KafkaMessageConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentKafkaConsumer implements KafkaMessageConsumer<PaymentInfo> {

    private final PaymentOutboxFacade paymentOutboxFacade;

    @Override
    @KafkaListener(
            topics = "${spring.kafka.topic.payment}",
            groupId = "${spring.kafka.consumer.payment.group-id}",
            containerFactory = "paymentKafkaListenerContainerFactory"
    )
    public void consume(PaymentInfo message) {
        log.info("consume message: {}", message);
        paymentOutboxFacade.publishMessageSuccess(message);
    }

}