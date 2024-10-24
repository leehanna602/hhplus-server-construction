package com.hhplus.server.domain.concert.model;

import com.hhplus.server.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "concert_seats")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConcertSeat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_schedule_id")
    private ConcertSchedule concertSchedule;

    @Column(name = "seat_num")
    private Integer seatNum;

    @Column(name = "price")
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_status")
    private SeatStatus seatStatus;

    public void temporaryReserved() {
        if (this.seatStatus != SeatStatus.AVAILABLE) {
            throw new IllegalStateException("좌석 예약이 불가능합니다.");
        }
        this.seatStatus = SeatStatus.TEMPORARY_RESERVED;
    }

    public void reservationCompleted() {
        this.seatStatus = SeatStatus.RESERVED;
    }

    public void concertSeatToAvailable() {
        this.seatStatus = SeatStatus.AVAILABLE;
    }

}