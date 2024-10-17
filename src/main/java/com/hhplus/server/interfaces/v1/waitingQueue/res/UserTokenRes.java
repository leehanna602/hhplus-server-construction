package com.hhplus.server.interfaces.v1.waitingQueue.res;

import com.hhplus.server.domain.waitingQueue.model.ProgressStatus;

public record UserTokenRes (
        String token,
        ProgressStatus progress,
        Long waitNum
) {
}
