package com.hhplus.server.domain.waitingQueue;

import com.hhplus.server.domain.waitingQueue.model.ProgressStatus;
import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;

import java.util.Optional;

public interface WaitingQueueReader {

    Optional<WaitingQueue> findByToken(String token);

    Long getWaitingNum(Long queueId);

    Optional<WaitingQueue> findByTokenAndProgress(String token, ProgressStatus progressStatus);
}
