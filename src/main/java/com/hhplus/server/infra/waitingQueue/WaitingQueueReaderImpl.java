package com.hhplus.server.infra.waitingQueue;

import com.hhplus.server.domain.waitingQueue.WaitingQueueReader;
import com.hhplus.server.domain.waitingQueue.model.ProgressStatus;
import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WaitingQueueReaderImpl implements WaitingQueueReader {

    private final WaitingQueueJpaRepository waitingQueueJpaRepository;

    @Override
    public Optional<WaitingQueue> findByToken(String token) {
        return waitingQueueJpaRepository.findByToken(token);
    }

    @Override
    public Long getWaitingNum(Long queueId) {
        return waitingQueueJpaRepository.getWaitingNum(queueId);
    }

    @Override
    public Optional<WaitingQueue> findByTokenAndProgress(String token, ProgressStatus progressStatus) {
        return waitingQueueJpaRepository.findByTokenAndProgress(token, progressStatus);
    }

    @Override
    public List<WaitingQueue> findByProgressOrderByQueueIdAsc(ProgressStatus progressStatus) {
        return waitingQueueJpaRepository.findByProgressOrderByQueueIdAsc(progressStatus);
    }

    @Override
    public List<WaitingQueue> findWaitingQueueToExpired() {
        return waitingQueueJpaRepository.findToExpiredToken();
    }

}
