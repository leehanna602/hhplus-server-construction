package com.hhplus.server.domain.concert.applicationEvent;

import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.infra.kafka.concert.ReservationKafkaProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class ReservationEventListener {

    private final ReservationKafkaProducer reservationKafkaProducer;

    public ReservationEventListener(ReservationKafkaProducer reservationKafkaProducer) {
        this.reservationKafkaProducer = reservationKafkaProducer;
    }

//    @Async
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void reservationSuccessHandler(ReservationInfo reservationInfo) throws InterruptedException {
//        log.info("ReservationEventListener reservationSuccessHandler start: {}", reservationInfo);
//        Thread.sleep(5000);
//        log.info("ReservationEventListener reservationSuccessHandler end: {}", reservationInfo);
//    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void reservationSuccessHandler(ReservationInfo reservationInfo) {
        reservationKafkaProducer.send(reservationInfo.toString());
    }


}
