package com.hhplus.server.domain.waitingQueue;

import com.hhplus.server.domain.support.exception.CommonException;
import com.hhplus.server.domain.waitingQueue.dto.WaitingQueueInfo;
import com.hhplus.server.domain.waitingQueue.model.ProgressStatus;
import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class WaitingQueueServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @Mock
    private SetOperations<String, String> setOperations;

    @InjectMocks
    private WaitingQueueService waitingQueueService;

    private final String waitingQueueKey = "waiting:concert";
    private final String activeQueueKey = "active:concert";
    private static final int WAITING_TO_ACTIVE_USER_NUMBER = 50;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
        when(redisTemplate.opsForSet()).thenReturn(setOperations);
    }

    @Test
    @DisplayName("토큰 생성시 WAITING Queue에 새로운 토큰 생성후 대기번호 조회")
    void whenGetWaitingQueue_thenGetWaitingQueue() {
        // given
        WaitingQueue waitingQueue = new WaitingQueue(UUID.randomUUID().toString(), System.currentTimeMillis());
        when(zSetOperations.add(waitingQueueKey, waitingQueue.token(), waitingQueue.expirationTimeMillis())).thenReturn(true);
        when(zSetOperations.rank(waitingQueueKey, waitingQueue.token())).thenReturn(1L);
        when(redisTemplate.expire(waitingQueueKey, 3600, TimeUnit.SECONDS)).thenReturn(true);

        // when
        WaitingQueueInfo waitingQueueInfo = waitingQueueService.generateToken();

        // then
        assertNotNull(waitingQueueInfo.token());
        assertEquals(ProgressStatus.WAITING, waitingQueueInfo.progress());
        verify(zSetOperations).rank(waitingQueueKey, waitingQueueInfo.token());
    }

    @Test
    @DisplayName("토큰 조회시 WAITING Queue에 존재해 토큰 조회 성공")
    void givenToken_whenGetWaitingQueue_thenGetWaitingQueue() {
        // given
        String token = UUID.randomUUID().toString();
        when(zSetOperations.rank(waitingQueueKey, token)).thenReturn(1L);

        // when
        WaitingQueueInfo waitingQueueInfo = waitingQueueService.getTokenStatus(token);

        // then
        assertNotNull(waitingQueueInfo);
        assertEquals(token, waitingQueueInfo.token());
        assertEquals(waitingQueueInfo.progress(), ProgressStatus.WAITING);
        assertEquals(waitingQueueInfo.waitingNum(), 1L);
        verify(zSetOperations).rank(waitingQueueKey, waitingQueueInfo.token());
    }

    @Test
    @DisplayName("토큰 조회시 WAITING Queue에 존재하지 않고 ACTIVE Queue에 존재해 토큰 조회 성공")
    void givenToken_whenGetActiveQueue_thenGetActiveQueue() {
        // given
        String token = UUID.randomUUID().toString();
        when(zSetOperations.rank(waitingQueueKey, token)).thenReturn(null);
        when(setOperations.isMember(activeQueueKey, token)).thenReturn(true);

        // when
        WaitingQueueInfo waitingQueueInfo = waitingQueueService.getTokenStatus(token);

        // then
        assertNotNull(waitingQueueInfo);
        assertEquals(token, waitingQueueInfo.token());
        assertEquals(waitingQueueInfo.progress(), ProgressStatus.ACTIVE);
        assertEquals(waitingQueueInfo.waitingNum(), 0L);
        verify(zSetOperations).rank(waitingQueueKey, waitingQueueInfo.token());
    }

    @Test
    @DisplayName("토큰이 WAITING, ACTIVE Queue에 모두에 존재하지 않아 에러 발생")
    void givenToken_notExistWaitingAndActiveQueue_thenThrowException() {
        // given
        String token = UUID.randomUUID().toString();
        when(zSetOperations.rank(waitingQueueKey, token)).thenReturn(null);
        when(setOperations.isMember(activeQueueKey, token)).thenReturn(false);

        // when & then
        assertThrows(CommonException.class, () -> waitingQueueService.getTokenStatus(token));
    }

    @Test
    @DisplayName("WAITING Queue에서 ACTIVE Queue로 WAITING_TO_ACTIVE_USER_NUMBER만큼 이동한다")
    void givenUsersInWaitingQueue_WhenMovingConfiguredNumber_ThenUpdateStatusToActive() {
        // given
        String token = UUID.randomUUID().toString();
        when(zSetOperations.range(waitingQueueKey, 0, WAITING_TO_ACTIVE_USER_NUMBER)).thenReturn(Set.of(token));
        when(redisTemplate.expire(activeQueueKey, 3600, TimeUnit.SECONDS)).thenReturn(true);
        when(zSetOperations.remove(waitingQueueKey, token)).thenReturn(1L);
        when(setOperations.add(waitingQueueKey, token)).thenReturn(1L);

        // when
        waitingQueueService.tokenProgressWaitingToActive();

        // then
        verify(zSetOperations).remove(waitingQueueKey, token);
        verify(setOperations).add(activeQueueKey, token);
    }

}