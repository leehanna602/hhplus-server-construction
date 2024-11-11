package com.hhplus.server.infra.waitingQueue;

import com.hhplus.server.domain.waitingQueue.RedisTokenWaitingQueue;
import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RedisTokenWaitingQueueImpl implements RedisTokenWaitingQueue {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisScript<Boolean> queueTransferScript;

    private final String waitingQueueKey = "waiting:concert";
    private final String activeQueueKey = "active:concert";

    @Override
    public void addWaitingQueueToken(WaitingQueue waitingQueue, int time, TimeUnit timeUnit) {
        redisTemplate.opsForZSet().add(waitingQueueKey, waitingQueue.token(), waitingQueue.expirationTimeMillis());
        redisTemplate.expire(waitingQueueKey, time, timeUnit);
    }

    @Override
    public Long getWaitingQueueTokenRank(String token) {
        return redisTemplate.opsForZSet().rank(waitingQueueKey, token);
    }

    @Override
    public Boolean isActiveToken(String token) {
        return redisTemplate.opsForSet().isMember(activeQueueKey, token);
    }

    @Override
    public Set<String> getWaitingToActiveUsers(int waitingToActiveUserNumber) {
        return redisTemplate.opsForZSet().range(waitingQueueKey, 0, waitingToActiveUserNumber-1);
    }

    @Override
    public void setExpireActiveQueue(int time, TimeUnit timeUnit) {
        redisTemplate.expire(activeQueueKey, time, timeUnit);
    }

    @Override
    public void transferWaitingToActiveQueue(String token) {
        List<String> keys = Arrays.asList(waitingQueueKey, activeQueueKey);
        redisTemplate.execute(queueTransferScript, keys, token);
    }

    @Override
    public void removeActiveToken(String token) {
        redisTemplate.opsForSet().remove(activeQueueKey, token);
    }


}
