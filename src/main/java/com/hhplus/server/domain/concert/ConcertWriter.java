package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.model.ConcertSeat;

public interface ConcertWriter {
    ConcertSeat save(ConcertSeat concertSeat);
}
