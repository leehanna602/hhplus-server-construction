package com.hhplus.server.domain.concert.applicationEvent;

import com.hhplus.server.domain.concert.ReservationOutboxService;
import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.infra.kafka.concert.ReservationKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReservationEventListener {

    private final ReservationOutboxService reservationOutboxService;
    private final ReservationKafkaProducer reservationKafkaProducer;


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void reservationSuccessHandler(ReservationInfo reservationInfo) {
        reservationOutboxService.initReservationOutbox(reservationInfo);
        reservationKafkaProducer.send(reservationInfo);
    }


}
