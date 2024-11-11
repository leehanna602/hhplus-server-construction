package com.hhplus.server.application.waitingQueue;

import com.hhplus.server.domain.waitingQueue.WaitingQueueService;
import com.hhplus.server.domain.waitingQueue.dto.WaitingQueueInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WaitingQueueFacade {

    private final WaitingQueueService waitingQueueService;

    public WaitingQueueInfo generateToken() {
        return waitingQueueService.generateToken();
    }

    public WaitingQueueInfo getTokenStatus(String token) {
        return waitingQueueService.getTokenStatus(token);
    }

}
