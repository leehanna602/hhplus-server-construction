package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.model.Concert;
import com.hhplus.server.domain.concert.model.ConcertSchedule;
import com.hhplus.server.domain.concert.model.ConcertSeat;

public interface ConcertWriter {

    Concert save(Concert concert);
    ConcertSchedule save(ConcertSchedule concertSchedule);
    ConcertSeat save(ConcertSeat concertSeat);
}
