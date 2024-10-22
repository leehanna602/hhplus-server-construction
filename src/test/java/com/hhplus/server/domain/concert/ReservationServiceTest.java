package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.domain.concert.model.*;
import com.hhplus.server.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationReader reservationReader;

    @Mock
    private ReservationWriter reservationWriter;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void 콘서트_임시예약_등록_성공() {
        // given
        User user = new User(1L, "user1");
        Concert concert = new Concert(132L, "concert1");
        ConcertSchedule concertSchedule = new ConcertSchedule(1L, concert,
                LocalDateTime.of(2024, 12, 10, 12, 0, 0), 50);

        ConcertSeat concertSeat = new ConcertSeat(1L, concert, concertSchedule, 5, 15000, SeatStatus.AVAILABLE);
        Reservation reservation = new Reservation(1L, user, concertSeat, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), ReservationStatus.TEMPORARY);
        when(reservationWriter.save(any(Reservation.class))).thenReturn(reservation);

        // when
        ReservationInfo reservationInfo = reservationService.concertSeatTemporalReservation(user, concertSeat);

        // then
        assertNotNull(reservationInfo);
        assertEquals(reservationInfo.status(), reservation.getReservationStatus());
        assertEquals(reservationInfo.expiredDt(), reservation.getReservationExpireDt());
    }
    
}