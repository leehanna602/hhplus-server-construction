package com.hhplus.server.infra.db.concert;

import com.hhplus.server.domain.concert.model.Concert;
import com.hhplus.server.domain.concert.model.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertSchedule, Long> {
    List<ConcertSchedule> findByConcert(Concert concert);

    ConcertSchedule findByConcertScheduleId(Long scheduleId);
}
