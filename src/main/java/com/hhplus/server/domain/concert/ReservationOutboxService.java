package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.domain.concert.model.ReservationOutbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationOutboxService {

    private final ReservationOutboxReader reservationOutboxReader;
    private final ReservationOutboxWriter reservationOutboxWriter;

    @Transactional
    public void updateStatusPublish(ReservationInfo reservationInfo) {
        ReservationOutbox reservationOutbox = reservationOutboxReader.findByReservationIdWithOptimisticLock(reservationInfo.reservationId());
        reservationOutbox.outBoxPublish();
        reservationOutboxWriter.save(reservationOutbox);
        log.info("reservation outbox status update publish: {}", reservationOutbox);
    }

}
