package com.hhplus.server.domain.payment.model;

import com.hhplus.server.domain.base.BaseEntity;
import com.hhplus.server.domain.concert.model.Reservation;
import com.hhplus.server.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "payment",
        indexes = {
                @Index(name = "payment_user_id_IDX", columnList = "user_id"),
                @Index(name = "payment_reservation_id_IDX", columnList = "reservation_id")
        }
)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(name = "amount")
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "pament_status")
    private PaymentStatus paymentStatus;

    @Column(name = "payment_dt")
    private LocalDateTime paymentDt;

    public Payment(User user, Reservation reservation, Integer amount, PaymentStatus paymentStatus) {
        this.user = user;
        this.reservation = reservation;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentDt = LocalDateTime.now();
    }

}