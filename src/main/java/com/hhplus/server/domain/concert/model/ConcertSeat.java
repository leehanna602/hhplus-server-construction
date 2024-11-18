package com.hhplus.server.domain.concert.model;

import com.hhplus.server.domain.support.exception.CommonException;
import com.hhplus.server.domain.support.exception.ConcertErrorCode;
import com.hhplus.server.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "concert_seats",
        indexes = {
                @Index(name = "concert_seats_concert_id_IDX", columnList = "concert_id"),
                @Index(name = "concert_seats_concert_schedule_id_IDX", columnList = "concert_schedule_id"),
                @Index(name = "concert_seats_concert_schedule_id_seat_status_IDX", columnList = "concert_schedule_id, seat_status")
        }
)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Version
    private Long version = 1L;

    public void temporaryReserved() {
        if (this.seatStatus != SeatStatus.AVAILABLE) {
            throw new CommonException(ConcertErrorCode.UNAVAILABLE_SEAT);
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