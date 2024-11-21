package com.hhplus.server.infra.db.concert;

import com.hhplus.server.domain.concert.ConcertWriter;
import com.hhplus.server.domain.concert.model.Concert;
import com.hhplus.server.domain.concert.model.ConcertSchedule;
import com.hhplus.server.domain.concert.model.ConcertSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConcertWriterImpl implements ConcertWriter {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;

    @Override
    public Concert save(Concert concert) {
        return concertJpaRepository.save(concert);
    }

    @Override
    public ConcertSchedule save(ConcertSchedule concertSchedule) {
        return concertScheduleJpaRepository.save(concertSchedule);
    }

    @Override
    public ConcertSeat save(ConcertSeat concertSeat) {
        return concertSeatJpaRepository.save(concertSeat);
    }
}
