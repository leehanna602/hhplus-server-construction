package com.hhplus.server.infra.db.concert;

import com.hhplus.server.domain.concert.model.ReservationOutbox;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationOutboxJpaRepository extends JpaRepository<ReservationOutbox, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    ReservationOutbox findByReservationId(Long reservationId);

}
