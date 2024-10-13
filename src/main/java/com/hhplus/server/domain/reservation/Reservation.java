package com.hhplus.server.domain.reservation;

import com.hhplus.server.domain.concert.ConcertSeat;
import com.hhplus.server.domain.user.User;
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
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private ConcertSeat seat;

    @Column(name = "reservation_dt")
    private LocalDateTime reservationDt;

    @Column(name = "reservation_expire_dt")
    private LocalDateTime reservationExpireDt;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status")
    private ReservationStatus reservationStatus;

}
