package com.hhplus.server.domain.concert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.server.domain.base.OutBoxStatus;
import com.hhplus.server.domain.concert.applicationEvent.ReservationEventPublisher;
import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.domain.concert.model.ReservationOutbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationOutboxService {

    private final ReservationOutboxReader reservationOutboxReader;
    private final ReservationOutboxWriter reservationOutboxWriter;
    private final ReservationEventPublisher reservationEventPublisher;
    private final ObjectMapper objectMapper;

    @Transactional
    public void initReservationOutbox(ReservationInfo reservationInfo) {
        try {
            String stringPayload = objectMapper.writeValueAsString(reservationInfo);
            reservationOutboxWriter.save(ReservationOutbox.create(stringPayload, reservationInfo.reservationId()));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @Transactional
    public void updateStatusPublish(ReservationInfo reservationInfo) {
        ReservationOutbox reservationOutbox = reservationOutboxReader.findByReservationIdWithOptimisticLock(reservationInfo.reservationId());
        reservationOutbox.outBoxPublish();
        reservationOutboxWriter.save(reservationOutbox);
        log.info("reservation outbox status update publish: {}", reservationOutbox);
    }

    public void reservationEventPublish() {
        List<ReservationOutbox> reservationOutboxList = reservationOutboxReader.findAllByOutboxStatusAndCreatedAtAfter(
                OutBoxStatus.INIT, LocalDateTime.now().minusMinutes(5)
        );
        log.info("update reservationOutboxList size: {}", reservationOutboxList.size());

        for (ReservationOutbox reservationOutbox : reservationOutboxList) {
            try {
                reservationEventPublisher.successReservation(objectMapper.readValue(reservationOutbox.getPayload(), ReservationInfo.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
