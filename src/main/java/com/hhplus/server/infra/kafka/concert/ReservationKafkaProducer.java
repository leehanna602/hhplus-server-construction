package com.hhplus.server.infra.kafka.concert;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationKafkaProducer {

    @Value("${spring.kafka.topic.reservation}")
    private String defaultTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String message) {
        log.info("ReservationKafkaProducer send message: {}", message);
        kafkaTemplate.send(defaultTopic, message);
    }

}