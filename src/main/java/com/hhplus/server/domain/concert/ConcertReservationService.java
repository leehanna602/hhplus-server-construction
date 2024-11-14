package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.applicationEvent.ReservationEventPublisher;
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

    private final ReservationEventPublisher reservationEventPublisher;

    @Transactional
    public ReservationInfo executeInReservationTransaction(User user, Long seatId) {
        // 1. 낙관적락 - 콘서트 좌석 조회
        ConcertSeat concertSeat = concertReader.findConcertSeatForReservationWithOptimisticLock(seatId)
                .orElseThrow(() -> new CommonException(ConcertErrorCode.INVALID_SEAT));
        log.info("get before seat: {}", concertSeat.getSeatStatus());
        
        // 2. 콘서트 좌석 임시예약 상태로 변경
        concertSeat.temporaryReserved();
        concertSeat = concertWriter.save(concertSeat);
        log.info("get after seat: {}", concertSeat.getSeatStatus());
        
        // 3. 예약 내역 저장
        Reservation reservation = reservationWriter.save(new Reservation(user, concertSeat));

        ReservationInfo reservationInfo = new ReservationInfo(
                reservation.getReservationId(), reservation.getSeat().getSeatId(),
                reservation.getReservationStatus(), reservation.getReservationExpireDt()
        );

        // 4. 예약 완료 이벤트 발행
        reservationEventPublisher.successReservation(reservationInfo);

        return reservationInfo;
    }

}
