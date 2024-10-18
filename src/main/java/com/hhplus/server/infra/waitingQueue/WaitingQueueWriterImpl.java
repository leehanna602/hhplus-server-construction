package com.hhplus.server.infra.waitingQueue;

import com.hhplus.server.domain.waitingQueue.WaitingQueueWriter;
import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingQueueWriterImpl implements WaitingQueueWriter {

    private final WaitingQueueJpaRepository waitingQueueJpaRepository;

    @Override
    public WaitingQueue save(WaitingQueue waitingQueue) {
        return waitingQueueJpaRepository.save(waitingQueue);
    }

}
