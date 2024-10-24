package com.hhplus.server.domain.concert.model;

import com.hhplus.server.domain.base.BaseEntity;
import com.hhplus.server.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private ConcertSeat seat;

    @Column(name = "reservation_dt")
    private LocalDateTime reservationDt;

    @Column(name = "reservation_expire_dt")
    private LocalDateTime reservationExpireDt;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status")
    private ReservationStatus reservationStatus;

    public Reservation(User user, ConcertSeat concertSeat) {
        this.user = user;
        this.seat = concertSeat;
        this.reservationDt = LocalDateTime.now();
        this.reservationExpireDt = LocalDateTime.now().plusMinutes(5);
        this.reservationStatus = ReservationStatus.TEMPORARY;
    }

    public void reservationCompleted() {
        this.reservationStatus = ReservationStatus.COMPLETED;
    }

    public void reservationExpired() {
        this.reservationStatus = ReservationStatus.EXPIRED;
    }

    public void validate() {
        if (reservationExpireDt.isBefore(LocalDateTime.now()) || reservationStatus == ReservationStatus.EXPIRED) {
            throw new RuntimeException("예약 가능한 시간이 만료된 예약 ID 입니다.");
        }
    }
}
