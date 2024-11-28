package com.hhplus.server.infra.db.concert;

import com.hhplus.server.domain.concert.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
    Reservation findByReservationId(Long reservationId);

    @Query("SELECT rv FROM Reservation rv WHERE rv.reservationStatus = 'TEMPORARY' AND current_timestamp > rv.reservationExpireDt")
    List<Reservation> findReservationsTemporaryToExpired();

}
