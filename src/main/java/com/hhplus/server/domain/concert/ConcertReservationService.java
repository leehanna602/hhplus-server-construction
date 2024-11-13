package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.support.exception.ConcertErrorCode;
import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.domain.concert.model.ConcertSeat;
import com.hhplus.server.domain.concert.model.Reservation;
import com.hhplus.server.domain.support.exception.CommonException;
import com.hhplus.server.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertReservationService {

    private final ConcertReader concertReader;
    private final ConcertWriter concertWriter;
    private final ReservationWriter reservationWriter;

    @Transactional
    public ReservationInfo executeInReservationTransaction(User user, Long seatId) {
        ConcertSeat concertSeat = concertReader.findConcertSeatForReservationWithOptimisticLock(seatId)
                .orElseThrow(() -> new CommonException(ConcertErrorCode.INVALID_SEAT));
        log.info("get before seat: {}", concertSeat.getSeatStatus());
        concertSeat.temporaryReserved();
        concertSeat = concertWriter.save(concertSeat);
        log.info("get after seat: {}", concertSeat.getSeatStatus());

        Reservation reservation = reservationWriter.save(new Reservation(user, concertSeat));
        return new ReservationInfo(
                reservation.getReservationId(), reservation.getSeat().getSeatId(),
                reservation.getReservationStatus(), reservation.getReservationExpireDt()
        );
    }

}
