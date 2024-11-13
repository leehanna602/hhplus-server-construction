package com.hhplus.server.domain.waitingQueue;

import com.hhplus.server.domain.support.exception.WaitingQueueErrorCode;
import com.hhplus.server.domain.support.exception.CommonException;
import com.hhplus.server.domain.waitingQueue.dto.WaitingQueueInfo;
import com.hhplus.server.domain.waitingQueue.model.ProgressStatus;
import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final RedisTokenWaitingQueue redisTokenWaitingQueue;

    private static final int WAITING_TO_ACTIVE_USER_NUMBER = 50;

    public WaitingQueueInfo generateToken() {
        WaitingQueue waitingQueue = WaitingQueue.of();
        log.info("waiting queue generate token: {}", waitingQueue);

        // waitingQueue TTL 설정 (1시간)
        redisTokenWaitingQueue.addWaitingQueueToken(waitingQueue, 3600, TimeUnit.SECONDS);

        Long rank = redisTokenWaitingQueue.getWaitingQueueTokenRank(waitingQueue.token());
        return new WaitingQueueInfo(waitingQueue.token(), ProgressStatus.WAITING, rank);
    }


    public WaitingQueueInfo getTokenStatus(String token) {
        Long rank = redisTokenWaitingQueue.getWaitingQueueTokenRank(token);
        log.info("rank: {}", rank);

        // Waiting queue에서 확인 후, 없으면 Active queue에서 확인
        if (rank == null) {
            if (Boolean.TRUE.equals(redisTokenWaitingQueue.isActiveToken(token))) {
                return new WaitingQueueInfo(token, ProgressStatus.ACTIVE, 0L);
            } else {
                throw new CommonException(WaitingQueueErrorCode.INVALID_TOKEN);
            }
        }

        return new WaitingQueueInfo(token, ProgressStatus.WAITING, rank);
    }


    public Boolean validateActiveToken(String token) {
        return redisTokenWaitingQueue.isActiveToken(token);
    }


    public void tokenProgressWaitingToActive() {
        // 호출할 때마다 정해진 인원만큼 Waiting queue에서 Active queue로 이동
        Set<String> waitingQueueTokens = redisTokenWaitingQueue.getWaitingToActiveUsers(WAITING_TO_ACTIVE_USER_NUMBER);

        // activeQueue TTL 설정 (1시간)
        redisTokenWaitingQueue.setExpireActiveQueue(3600, TimeUnit.SECONDS);

        assert waitingQueueTokens != null;
        for (String token : waitingQueueTokens) {
            redisTokenWaitingQueue.transferWaitingToActiveQueue(token);
        }
    }


    public void removeActiveToken(String token) {
        redisTokenWaitingQueue.removeActiveToken(token);
    }

}
