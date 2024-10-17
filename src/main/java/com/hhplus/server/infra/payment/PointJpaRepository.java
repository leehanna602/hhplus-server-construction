package com.hhplus.server.infra.payment;

import com.hhplus.server.domain.payment.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointJpaRepository extends JpaRepository<Point, Long> {
}
