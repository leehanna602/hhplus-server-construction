package com.hhplus.server.infra.kafka.payment;

import com.hhplus.server.domain.payment.dto.PaymentInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentKafkaProducer {

    @Value("${spring.kafka.topic.payment}")
    private String defaultTopic;

    private final KafkaTemplate<String, PaymentInfo> kafkaTemplate;

    public void send(PaymentInfo message) {
        log.info("PaymentKafkaProducer send message: {}", message);
        kafkaTemplate.send(defaultTopic, message);
    }

}