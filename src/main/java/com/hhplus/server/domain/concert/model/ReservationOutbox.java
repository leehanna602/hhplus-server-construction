package com.hhplus.server.domain.concert.model;

import com.hhplus.server.domain.base.BaseOutBox;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "reservation_outbox",
        indexes = {
                @Index(name = "reservation_id_IDX", columnList = "reservation_id"),
                @Index(name = "reservation_outbox_status_created_IDX", columnList = "outbox_status, created_at")
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationOutbox extends BaseOutBox {

    @Column(name = "reservation_id")
    private Long reservationId;

    @Builder
    public ReservationOutbox(String payload, Long reservationId) {
        super(payload);
        this.reservationId = reservationId;
    }

    public static ReservationOutbox create(String payload, Long reservationId) {
        return ReservationOutbox.builder()
                .payload(payload)
                .reservationId(reservationId)
                .build();
    }

}
