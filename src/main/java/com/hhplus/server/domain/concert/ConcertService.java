package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.dto.ConcertScheduleInfo;
import com.hhplus.server.domain.concert.dto.ConcertSeatInfo;
import com.hhplus.server.domain.concert.model.Concert;
import com.hhplus.server.domain.concert.model.ConcertSchedule;
import com.hhplus.server.domain.concert.model.ConcertSeat;
import com.hhplus.server.domain.concert.model.SeatStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertReader concertReader;
    private final ConcertWriter concertWriter;

    @Transactional(readOnly = true)
    public ConcertScheduleInfo getConcertSchedule(long concertId) {
        Concert concert = concertReader.getConcert(concertId);
        List<ConcertSchedule> concertSchedules = concertReader.getConcertSchedules(concert);
        List<ConcertScheduleInfo.ConcertSchedules> concertSchedulesList = concertSchedules.stream()
                .map(concertSchedule -> new ConcertScheduleInfo.ConcertSchedules(
                        concertSchedule.getConcertScheduleId(),
                        concertSchedule.getConcertDt(),
                        concertSchedule.getTotalSeat()
                ))
                .toList();

        return new ConcertScheduleInfo(concertId, concertSchedulesList);
    }

    @Transactional(readOnly = true)
    public ConcertSeatInfo getConcertSeats(long concertId, long scheduleId) {
        ConcertSchedule concertSchedule = concertReader.getConcertSchedules(scheduleId);
        List<ConcertSeat> concertSeats = concertReader.getConcertSeatsBySeatStatus(concertSchedule, SeatStatus.AVAILABLE);
        List<ConcertSeatInfo.SeatInfo> concertSeatsList = concertSeats.stream()
                .map(concertSeat -> new ConcertSeatInfo.SeatInfo(
                        concertSeat.getSeatId(),
                        concertSeat.getSeatNum(),
                        concertSeat.getPrice()
                ))
                .toList();

        return new ConcertSeatInfo(concertId, scheduleId, concertSchedule.getTotalSeat(),
                concertSeatsList.size(), concertSeatsList);
    }

    @Transactional
    public ConcertSeat findConcertSeatForReservation(Long concertId, Long scheduleId, Long seatId) {
        ConcertSeat concertSeat = concertReader.findConcertSeatForReservationWithLock(concertId, scheduleId, seatId)
                .orElseThrow(() -> new RuntimeException("존재하는 좌석이 없습니다."));

        concertSeat.temporaryReserved();
        concertSeat = concertWriter.save(concertSeat);
        return concertSeat;
    }

    @Transactional
    public ConcertSeat completeStatus(ConcertSeat concertSeat) {
        concertSeat.reservationCompleted();
        concertSeat = concertWriter.save(concertSeat);
        return concertSeat;
    }

    @Transactional
    public void concertSeatToExpired(ConcertSeat concertSeat) {
        concertSeat.concertSeatToAvailable();
        concertWriter.save(concertSeat);
    }

}
