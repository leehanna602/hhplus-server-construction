package com.hhplus.server.infra.payment;

import com.hhplus.server.domain.payment.model.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {
}
