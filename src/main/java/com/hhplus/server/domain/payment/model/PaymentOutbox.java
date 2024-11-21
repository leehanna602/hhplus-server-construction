package com.hhplus.server.domain.payment.model;

import com.hhplus.server.domain.base.BaseOutBox;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
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
public class PaymentOutbox extends BaseOutBox {

    @Column(name = "payment_id")
    private Long paymentId;

    public PaymentOutbox(String payload, Long paymentId) {
        super(payload);
        this.paymentId = paymentId;
    }

}
