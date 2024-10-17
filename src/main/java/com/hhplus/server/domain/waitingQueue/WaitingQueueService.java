package com.hhplus.server.domain.waitingQueue;

import com.hhplus.server.domain.waitingQueue.dto.WaitingQueueInfo;
import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final WaitingQueueReader waitingQueueReader;
    private final WaitingQueueWriter waitingQueueWriter;

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
                throw new RuntimeException("존재하지 않은 토큰입니다.");
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

}
