package com.hhplus.server.interfaces.v1.concert;

import com.hhplus.server.application.concert.ConcertFacade;
import com.hhplus.server.domain.concert.dto.ConcertScheduleInfo;
import com.hhplus.server.domain.concert.dto.ConcertSeatInfo;
import com.hhplus.server.interfaces.v1.concert.res.ConcertScheduleRes;
import com.hhplus.server.interfaces.v1.concert.res.ConcertSeatsRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/concert")
public class ConcertController {

    private final ConcertFacade concertFacade;

    /* 예약 가능 날짜 조회 */
    @GetMapping("/scheduled/{concertId}")
    public ResponseEntity<ConcertScheduleRes> getAvailableDates(@PathVariable long concertId) {
        ConcertScheduleInfo concertScheduleInfo = concertFacade.getConcertSchedule(concertId);

        List<ConcertScheduleRes.ConcertScheduleData> scheduleData = concertScheduleInfo.concertSchedulesList().stream()
                .map(schedule -> new ConcertScheduleRes.ConcertScheduleData(
                        schedule.concertScheduleId(),
                        schedule.concertDt()
                ))
                .toList();

        return ResponseEntity.ok(new ConcertScheduleRes(concertId, scheduleData));
    }

    /* 예약 가능 좌석 조회 */
    @GetMapping("/{concertId}/{scheduleId}/seats")
    public ResponseEntity<ConcertSeatsRes> getAvailableSeats(@PathVariable long concertId, @PathVariable long scheduleId) {
        ConcertSeatInfo concertSeatInfo = concertFacade.getConcertSeats(concertId, scheduleId);
        List<ConcertSeatsRes.SeatsRes> seatsRes = concertSeatInfo.concertSeatsList().stream()
                .map(concertSeat -> new ConcertSeatsRes.SeatsRes(
                        concertSeat.seatId(),
                        concertSeat.seatNum(),
                        concertSeat.price()
                ))
                .toList();

        ConcertSeatsRes concertSeatsRes = new ConcertSeatsRes(concertId, scheduleId, concertSeatInfo.totalSeatCnt(),
                concertSeatInfo.availableSeatCnt(), seatsRes);

        return ResponseEntity.ok(concertSeatsRes);
    }
    
}
