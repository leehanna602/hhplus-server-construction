package com.hhplus.server.interfaces.kafka.concert;

import com.hhplus.server.application.concert.ReservationOutboxFacade;
import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.interfaces.kafka.KafkaMessageConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReservationKafkaConsumer implements KafkaMessageConsumer<ReservationInfo> {

    private final ReservationOutboxFacade reservationOutboxFacade;


    @Override
    @KafkaListener(
            topics = "${spring.kafka.topic.reservation}",
            groupId = "${spring.kafka.consumer.reservation.group-id}",
            containerFactory = "reservationKafkaListenerContainerFactory"
    )
    public void consume(@Payload ReservationInfo message) {
        log.info("consume message: {}", message);
        reservationOutboxFacade.publishMessageSuccess(message);
    }

}