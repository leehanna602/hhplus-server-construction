package com.hhplus.server.application.concert.scheduling;

import com.hhplus.server.domain.concert.ReservationOutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReservationOutboxScheduler {

    private final ReservationOutboxService reservationOutboxService;

    @Scheduled(fixedRate = 5000)
    public void successReservationEventPublish() {
        reservationOutboxService.reservationEventPublish();
    }

}
