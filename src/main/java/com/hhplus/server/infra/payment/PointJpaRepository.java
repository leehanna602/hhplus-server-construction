package com.hhplus.server.infra.payment;

import com.hhplus.server.domain.payment.model.Point;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface PointJpaRepository extends JpaRepository<Point, Long> {
    @Query("SELECT po FROM Point po " +
            "WHERE po.user.userId = :userId")
    Point findByUser(Long userId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT po FROM Point po " +
            "WHERE po.user.userId = :userId")
    Point findByUserPointWithOptimisticLock(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "javax.persistence.lock.timeout", value = "3000"))
//    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT po FROM Point po " +
            "WHERE po.user.userId = :userId")
    Point findByUserPointWithPessimisticLock(Long userId);

}
