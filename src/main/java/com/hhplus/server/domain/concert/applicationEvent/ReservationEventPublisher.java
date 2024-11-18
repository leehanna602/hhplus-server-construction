package com.hhplus.server.domain.concert.applicationEvent;

import com.hhplus.server.domain.concert.dto.ReservationInfo;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ReservationEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public ReservationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void successReservation(ReservationInfo reservationInfo) {
        applicationEventPublisher.publishEvent(reservationInfo);
    }

}
