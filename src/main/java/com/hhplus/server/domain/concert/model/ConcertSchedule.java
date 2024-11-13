package com.hhplus.server.domain.concert.model;

import com.hhplus.server.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "concert_schedule",
        indexes = {
                @Index(name = "concert_schedule_concert_id_IDX", columnList = "concert_id")
        }
)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConcertSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_schedule_id")
    private Long concertScheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Column(name = "concert_dt")
    private LocalDateTime concertDt;

    @Column(name = "total_seat")
    private Integer totalSeat;

}