package com.hhplus.server.domain.waitingQueue.dto;

import com.hhplus.server.domain.waitingQueue.model.ProgressStatus;

import java.time.LocalDateTime;

public record WaitingQueueInfo(
        Long queueId,
        String token,
        ProgressStatus progress,
        LocalDateTime expiredAt,
        Long waitingNum
) {
}
