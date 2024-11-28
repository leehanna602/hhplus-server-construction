package com.hhplus.server.domain.payment.model;

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
        name = "payment_outbox",
        indexes = {
                @Index(name = "payment_id_IDX", columnList = "payment_id"),
                @Index(name = "payment_outbox_status_created_IDX", columnList = "outbox_status, created_at")
        }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOutbox extends BaseOutBox {

    @Column(name = "payment_id")
    private Long paymentId;

    @Builder
    public PaymentOutbox(String payload, Long paymentId) {
        super(payload);
        this.paymentId = paymentId;
    }

    public static PaymentOutbox create(String payload, Long paymentId) {
        return PaymentOutbox.builder()
                .payload(payload)
                .paymentId(paymentId)
                .build();
    }

}
