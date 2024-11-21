package com.hhplus.server.infra.db.concert;

import com.hhplus.server.domain.concert.model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {
    Concert findByConcertId(long concertId);
}
