package com.hhplus.server.interfaces.api;

import com.hhplus.server.interfaces.dto.res.ConcertScheduleRes;
import com.hhplus.server.interfaces.dto.res.ConcertSeatsRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/concert")
public class ConcertController {
    
    /* 예약 가능 날짜 조회 */
    @GetMapping("/scheduled/{concertId}")
    public ResponseEntity<ConcertScheduleRes> getAvailableDates(@PathVariable long concertId) {
        return ResponseEntity.ok(new ConcertScheduleRes(concertId, Arrays.asList("2024-10-15", "2024-10-16", "2024-10-17")));
    }
    
    /* 예약 가능 좌석 조회 */
    @GetMapping("/{concertId}/{date}/seats")
    public ResponseEntity<ConcertSeatsRes> getAvailableSeats(@PathVariable long concertId, @PathVariable String date) {
        return ResponseEntity.ok(new ConcertSeatsRes(concertId, 35L, 1, new ArrayList<>()));
    }
    
}
