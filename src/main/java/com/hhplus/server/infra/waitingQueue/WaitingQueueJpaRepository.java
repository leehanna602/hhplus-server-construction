package com.hhplus.server.infra.waitingQueue;

import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueue, Long> {
}
