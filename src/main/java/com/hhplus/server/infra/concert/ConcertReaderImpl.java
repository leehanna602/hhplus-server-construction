package com.hhplus.server.infra.concert;

import com.hhplus.server.domain.concert.ConcertReader;
import com.hhplus.server.domain.concert.model.Concert;
import com.hhplus.server.domain.concert.model.ConcertSchedule;
import com.hhplus.server.domain.concert.model.ConcertSeat;
import com.hhplus.server.domain.concert.model.SeatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertReaderImpl implements ConcertReader {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;

    @Override
    public List<ConcertSchedule> getConcertSchedules(Concert concert) {
        return concertScheduleJpaRepository.findByConcert(concert);
    }

    @Override
    public ConcertSchedule getConcertSchedules(Long scheduleId) {
        return concertScheduleJpaRepository.findByConcertScheduleId(scheduleId);
    }

    @Override
    public Concert getConcert(long concertId) {
        return concertJpaRepository.findByConcertId(concertId);
    }

    @Override
    public List<ConcertSeat> getConcertSeatsBySeatStatus(ConcertSchedule concertSchedule, SeatStatus seatStatus) {
        return concertSeatJpaRepository.findByConcertScheduleAndSeatStatus(concertSchedule, seatStatus);
    }

    @Override
    public Optional<ConcertSeat> findConcertSeatForReservationWithPessimisticLock(Long concertId, Long scheduleId, Long seatId) {
        return concertSeatJpaRepository.findConcertSeatForReservationWithPessimisticLock(concertId, scheduleId, seatId);
    }
}
