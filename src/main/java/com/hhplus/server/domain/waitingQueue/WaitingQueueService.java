package com.hhplus.server.domain.waitingQueue;

import com.hhplus.server.domain.support.exception.CommonException;
import com.hhplus.server.domain.common.exception.WaitingQueueErrorCode;
import com.hhplus.server.domain.waitingQueue.dto.WaitingQueueInfo;
import com.hhplus.server.domain.waitingQueue.model.ProgressStatus;
import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final WaitingQueueReader waitingQueueReader;
    private final WaitingQueueWriter waitingQueueWriter;

    private static final int activeProgressUserNumber = 50;

    @Transactional
    public WaitingQueueInfo getWaitingQueueInfo(String token) {
        WaitingQueue waitingQueue;
        Optional<WaitingQueue> existWaitingQueue = waitingQueueReader.findByToken(token);

        // token 존재
        if (existWaitingQueue.isPresent()) {
            waitingQueue = existWaitingQueue.get();
            waitingQueue.validateToken();
        } else {
            if (token != null) {
                throw new CommonException(WaitingQueueErrorCode.INVALID_TOKEN);
            }
            // token 생성
            waitingQueue = waitingQueueWriter.save(new WaitingQueue());
        }

        Long waitingNum = waitingQueueReader.getWaitingNum(waitingQueue.getQueueId());
        return new WaitingQueueInfo(
                waitingQueue.getQueueId(),
                waitingQueue.getToken(),
                waitingQueue.getProgress(),
                waitingQueue.getExpiredAt(),
                waitingNum
        );
    }

    @Transactional(readOnly = true)
    public boolean validateActiveToken(String token) {
        Optional<WaitingQueue> waitingQueue = waitingQueueReader.findByTokenAndProgress(token, ProgressStatus.ACTIVE);
        return waitingQueue.isPresent();
    }

    @Transactional
    public void tokenProgressWaitingToActive() {
        List<WaitingQueue> waitingList = waitingQueueReader.findByProgressOrderByQueueIdAsc(ProgressStatus.WAITING);
        List<WaitingQueue> activeList = waitingQueueReader.findByProgressOrderByQueueIdAsc(ProgressStatus.ACTIVE);

        int addActiveNumber = activeProgressUserNumber - activeList.size();
        if (!waitingList.isEmpty() && addActiveNumber > 0) {
            for (WaitingQueue waitingQueue : waitingList) {
                waitingQueue.waitingToActiveToken();
                waitingQueueWriter.save(waitingQueue);
            }
        }
    }

    @Transactional
    public void expiredToken() {
        List<WaitingQueue> toExpiredList = waitingQueueReader.findWaitingQueueToExpired();
        for (WaitingQueue waitingQueue : toExpiredList) {
            waitingQueue.expireToken();
            waitingQueueWriter.save(waitingQueue);
        }
    }

    @Transactional
    public void expiredToken(String token) {
        Optional<WaitingQueue> findWaitingQueue = waitingQueueReader.findByToken(token);
        if (findWaitingQueue.isPresent()) {
            WaitingQueue waitingQueue = findWaitingQueue.get();
            waitingQueue.expireToken();
            waitingQueueWriter.save(waitingQueue);
        }
    }

    public WaitingQueue save(WaitingQueue waitingQueue) {
        return waitingQueueWriter.save(waitingQueue);
    }

}
