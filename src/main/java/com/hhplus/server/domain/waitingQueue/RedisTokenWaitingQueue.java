package com.hhplus.server.domain.waitingQueue;

import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisTokenWaitingQueue {

    void addWaitingQueueToken(WaitingQueue waitingQueue, int time, TimeUnit timeUnit);

    Long getWaitingQueueTokenRank(String token);

    Boolean isActiveToken(String token);

    Set<String> getWaitingToActiveUsers(int waitingToActiveUserNumber);

    void setExpireActiveQueue(int i, TimeUnit timeUnit);

    void transferWaitingToActiveQueue(String token);

    void removeActiveToken(String token);
}
