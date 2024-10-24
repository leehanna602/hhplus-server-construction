package com.hhplus.server.application.waitingQueue.scheduling;

import com.hhplus.server.domain.waitingQueue.WaitingQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Slf4j
@RequiredArgsConstructor
public class WaitingQueueScheduler {

    private final WaitingQueueService waitingQueueService;

    /* WAITING -> ACTIVE 상태 변경 */
    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void updateWaitingQueue() {
        waitingQueueService.tokenProgressWaitingToActive();
    }

    /* WAITING, ACTIVE -> EXPIRED 상태 변경 */
    @Scheduled(cron = "*/20 * * * * *")
    @Transactional
    public void expiredWaitingQueue() {
        waitingQueueService.expiredToken();
    }

}
