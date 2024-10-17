package com.hhplus.server.domain.waitingQueue;

import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;

public interface WaitingQueueWriter {
    WaitingQueue save(WaitingQueue waitingQueue);
}
