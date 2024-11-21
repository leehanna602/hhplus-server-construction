package com.hhplus.server.interfaces.kafka.payment;

import com.hhplus.server.interfaces.kafka.KafkaMessageConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PaymentKafkaConsumer implements KafkaMessageConsumer {

    @Override
    @KafkaListener(
            topics = "${spring.kafka.topic.payment}",
            groupId = "${spring.kafka.consumer.payment.group-id}"
    )
    public void consume(String message) {
        log.info("consume message: {}", message);
    }

}