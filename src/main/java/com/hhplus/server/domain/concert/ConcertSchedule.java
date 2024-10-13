package com.hhplus.server.domain.concert;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "concert_schedule")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConcertSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_schedule_id")
    private Long concertScheduleId;

    @ManyToOne
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Column(name = "concert_dt")
    private LocalDateTime concertDt;

    @Column(name = "total_seat")
    private Integer totalSeat;

}