package com.hhplus.server.infra.concert;

import com.hhplus.server.domain.concert.model.ConcertSchedule;
import com.hhplus.server.domain.concert.model.ConcertSeat;
import com.hhplus.server.domain.concert.model.SeatStatus;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertSeatJpaRepository extends JpaRepository<ConcertSeat, Long> {

    List<ConcertSeat> findByConcertScheduleAndSeatStatus(ConcertSchedule concertSchedule, SeatStatus seatStatus);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT cs FROM ConcertSeat cs " +
            "WHERE cs.concert.concertId = :concertId " +
            "AND cs.concertSchedule.concertScheduleId = :scheduleId " +
            "AND cs.seatId = :seatId")
    Optional<ConcertSeat> findConcertSeatForReservationWithOptimisticLock(Long concertId, Long scheduleId, Long seatId);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "javax.persistence.lock.timeout", value = "3000"))
    @Query("SELECT cs FROM ConcertSeat cs " +
            "WHERE cs.concert.concertId = :concertId " +
            "AND cs.concertSchedule.concertScheduleId = :scheduleId " +
            "AND cs.seatId = :seatId")
    Optional<ConcertSeat> findConcertSeatForReservationWithPessimisticLock(Long concertId, Long scheduleId, Long seatId);

}
