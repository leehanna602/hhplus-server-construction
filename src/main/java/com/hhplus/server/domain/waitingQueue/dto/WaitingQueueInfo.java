package com.hhplus.server.domain.waitingQueue.dto;

import com.hhplus.server.domain.waitingQueue.model.ProgressStatus;


public record WaitingQueueInfo(
        String token,
        ProgressStatus progress,
        Long waitingNum
) {
}
