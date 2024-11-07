package com.hhplus.server.domain.waitingQueue;

import com.hhplus.server.domain.common.exception.WaitingQueueErrorCode;
import com.hhplus.server.domain.support.exception.CommonException;
import com.hhplus.server.domain.waitingQueue.dto.WaitingQueueInfo;
import com.hhplus.server.domain.waitingQueue.model.ProgressStatus;
import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingQueueService {

    private final RedisTemplate<String, String> redisTemplate;

    private final String waitingQueueKey = "waiting:concert";
    private final String activeQueueKey = "active:concert";

    private static final int WAITING_TO_ACTIVE_USER_NUMBER = 50;

    public WaitingQueueInfo generateToken() {
        WaitingQueue waitingQueue = WaitingQueue.of();
        log.info("waiting queue generate token: {}", waitingQueue);
        redisTemplate.opsForZSet().add(waitingQueueKey, waitingQueue.token(),waitingQueue.expirationTimeMillis());

        // waitingQueue TTL 설정 (1시간)
        redisTemplate.expire(waitingQueueKey, 3600, TimeUnit.SECONDS);

        Long rank = redisTemplate.opsForZSet().rank(waitingQueueKey, waitingQueue.token());
        return new WaitingQueueInfo(waitingQueue.token(), ProgressStatus.WAITING, rank);
    }


    public WaitingQueueInfo getTokenStatus(String token) {
        Long rank = redisTemplate.opsForZSet().rank(waitingQueueKey, token);
        log.info("rank: {}", rank);

        // Waiting queue에서 확인 후, 없으면 Active queue에서 확인
        if (rank == null) {
            if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(activeQueueKey, token))) {
                return new WaitingQueueInfo(token, ProgressStatus.ACTIVE, 0L);
            } else {
                throw new CommonException(WaitingQueueErrorCode.INVALID_TOKEN);
            }
        }

        return new WaitingQueueInfo(token, ProgressStatus.WAITING, rank);
    }


    public Boolean validateActiveToken(String token) {
        return redisTemplate.opsForSet().isMember(activeQueueKey, token);
    }


    public void tokenProgressWaitingToActive() {
        // 호출할 때마다 정해진 인원만큼 Waiting queue에서 Active queue로 이동
        Set<String> waitingQueueTokens = redisTemplate.opsForZSet().range(waitingQueueKey, 0, WAITING_TO_ACTIVE_USER_NUMBER);

        // activeQueue TTL 설정 (1시간)
        redisTemplate.expire(activeQueueKey, 3600, TimeUnit.SECONDS);

        assert waitingQueueTokens != null;
        for (String token : waitingQueueTokens) {
            redisTemplate.opsForZSet().remove(waitingQueueKey, token);
            redisTemplate.opsForSet().add(activeQueueKey, token);
        }
    }


    public void removeActiveToken(String token) {
        redisTemplate.opsForSet().remove(activeQueueKey, token);
    }

}
