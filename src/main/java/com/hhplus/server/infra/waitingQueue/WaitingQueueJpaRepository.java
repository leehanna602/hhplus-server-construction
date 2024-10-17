package com.hhplus.server.infra.waitingQueue;

import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueue, Long> {
    Optional<WaitingQueue> findByToken(String token);

    @Query("SELECT COUNT(*) FROM WaitingQueue WHERE progress = 'WAITING' AND queueId <= :queueId")
    Long getWaitingNum(Long queueId);

}
