package com.hhplus.server.interfaces.v1.concert;

import com.hhplus.server.application.concert.ReservationFacade;
import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.interfaces.v1.concert.req.ReservationReq;
import com.hhplus.server.interfaces.v1.concert.res.ReservationRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reservation")
public class ReservationController {

    private final ReservationFacade reservationFacade;

    /* 좌석 예약 요청 */
    @PostMapping("/seat")
    public ResponseEntity<ReservationRes> reservation(@RequestHeader("token") String token, @RequestBody ReservationReq reservationReq) {
        ReservationInfo reservationInfo = reservationFacade.concertSeatReservation(reservationReq);
        ReservationRes reservationRes = new ReservationRes(reservationInfo.reservationId(), reservationInfo.seatId(),
                reservationInfo.status(), reservationInfo.expiredDt().toString());
        return ResponseEntity.ok(reservationRes);
    }

}