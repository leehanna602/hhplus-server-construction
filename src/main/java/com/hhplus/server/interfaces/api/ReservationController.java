package com.hhplus.server.interfaces.api;

import com.hhplus.server.domain.reservation.ReservationStatus;
import com.hhplus.server.interfaces.dto.req.ReservationReq;
import com.hhplus.server.interfaces.dto.res.ReservationRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/reservation")
public class ReservationController {

    /* 좌석 예약 요청 */
    @PostMapping("/seat")
    public ResponseEntity<ReservationRes> reservation(@RequestBody ReservationReq reservationReq) {
        return ResponseEntity.ok(new ReservationRes(1233L, reservationReq.seatId(),
                ReservationStatus.TEMPORARY, LocalDateTime.now().toString()));
    }

}
