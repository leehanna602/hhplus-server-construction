package com.hhplus.server.interfaces.v1.concert;

import com.hhplus.server.interfaces.v1.concert.res.ConcertScheduleRes;
import com.hhplus.server.interfaces.v1.concert.res.ConcertSeatsRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/concert")
public class ConcertController {
    
    /* 예약 가능 날짜 조회 */
    @GetMapping("/scheduled/{concertId}")
    public ResponseEntity<ConcertScheduleRes> getAvailableDates(@PathVariable long concertId) {
        return ResponseEntity.ok(new ConcertScheduleRes(concertId, new ArrayList<>()));
    }
    
    /* 예약 가능 좌석 조회 */
    @GetMapping("/{concertId}/{scheduleId}/seats")
    public ResponseEntity<ConcertSeatsRes> getAvailableSeats(@PathVariable long concertId, @PathVariable long scheduleId) {
        return ResponseEntity.ok(new ConcertSeatsRes(concertId, 35L, 50, 1, new ArrayList<>()));
    }
    
}
