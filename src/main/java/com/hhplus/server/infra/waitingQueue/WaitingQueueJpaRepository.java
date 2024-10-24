package com.hhplus.server.infra.waitingQueue;

import com.hhplus.server.domain.waitingQueue.model.ProgressStatus;
import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueue, Long> {
    Optional<WaitingQueue> findByToken(String token);

    @Query("SELECT COUNT(*) FROM WaitingQueue WHERE progress = 'WAITING' AND queueId <= :queueId")
    Long getWaitingNum(Long queueId);

    Optional<WaitingQueue> findByTokenAndProgress(String token, ProgressStatus progressStatus);

    List<WaitingQueue> findByProgressOrderByQueueIdAsc(ProgressStatus progressStatus);

    @Query("SELECT wq FROM WaitingQueue wq WHERE wq.progress <> 'EXPIRED' AND current_timestamp > wq.expiredAt")
    List<WaitingQueue> findToExpiredToken();

}
