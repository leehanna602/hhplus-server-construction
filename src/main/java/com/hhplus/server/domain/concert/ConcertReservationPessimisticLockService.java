package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.support.exception.ConcertErrorCode;
import com.hhplus.server.domain.concert.model.ConcertSeat;
import com.hhplus.server.domain.support.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertReservationPessimisticLockService {

    private final ConcertReader concertReader;
    private final ConcertWriter concertWriter;

    /* 비관적락 */
    @Transactional
    public ConcertSeat findConcertSeatForReservationWithPessimisticLock(Long seatId) {
        ConcertSeat concertSeat = concertReader.findConcertSeatForReservationWithPessimisticLock(seatId)
                .orElseThrow(() -> new CommonException(ConcertErrorCode.INVALID_SEAT));

        concertSeat.temporaryReserved();
        concertSeat = concertWriter.save(concertSeat);
        return concertSeat;
    }


}
