package com.hhplus.server.application.concert;

import com.hhplus.server.domain.concert.ReservationOutboxService;
import com.hhplus.server.domain.concert.dto.ReservationInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ReservationOutboxFacade {

    private final ReservationOutboxService reservationOutBoxService;

    public void publishMessageSuccess(ReservationInfo message) {
        reservationOutBoxService.updateStatusPublish(message);
    }

}
