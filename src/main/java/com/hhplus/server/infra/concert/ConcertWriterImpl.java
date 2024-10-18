package com.hhplus.server.infra.concert;

import com.hhplus.server.domain.concert.ConcertWriter;
import com.hhplus.server.domain.concert.model.ConcertSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConcertWriterImpl implements ConcertWriter {

    private final ConcertSeatJpaRepository concertSeatJpaRepository;

    @Override
    public ConcertSeat save(ConcertSeat concertSeat) {
        return concertSeatJpaRepository.save(concertSeat);
    }
}
