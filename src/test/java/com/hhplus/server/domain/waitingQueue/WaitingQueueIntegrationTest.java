package com.hhplus.server.domain.waitingQueue;

import com.hhplus.server.application.waitingQueue.WaitingQueueFacade;
import com.hhplus.server.domain.support.exception.CommonException;
import com.hhplus.server.domain.waitingQueue.dto.WaitingQueueInfo;
import com.hhplus.server.domain.waitingQueue.model.ProgressStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class WaitingQueueIntegrationTest {

    @Autowired
    private WaitingQueueFacade waitingQueueFacade;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private final String waitingQueueKey = "waiting:concert";
    private final String activeQueueKey = "active:concert";

    @BeforeEach
    void setUp() {
        redisTemplate.delete(waitingQueueKey);
        redisTemplate.delete(activeQueueKey);
    }

    @Test
    @DisplayName("대기열 토큰 생성 요청 시 토큰이 생성되고 토큰 정보, 상태, 대기번호를 반환한다.")
    void givenTokenRequest_whenGenerateWaitingQueue_thenGenerateWaitingQueueInfo() {
        // given & when
        WaitingQueueInfo waitingQueueInfo = waitingQueueFacade.generateToken();

        // then
        assertThat(waitingQueueInfo).isNotNull();
        assertThat(waitingQueueInfo.token()).isNotNull();
        assertThat(waitingQueueInfo.waitingNum()).isEqualTo(0L);
        assertThat(waitingQueueInfo.progress()).isEqualTo(ProgressStatus.WAITING);
    }

    @Test
    @DisplayName("토큰 상태 확인 요청 시 WAITING 상태와 대기번호를 반환한다.")
    void givenToken_whenCheckTokenStatus_thenExistTokenInWaitingQueue() {
        // given
        WaitingQueueInfo generateToken = waitingQueueFacade.generateToken();

        // when
        WaitingQueueInfo tokenStatus = waitingQueueFacade.getTokenStatus(generateToken.token());

        // then
        assertThat(tokenStatus).isNotNull();
        assertThat(tokenStatus.token()).isEqualTo(generateToken.token());
        assertThat(tokenStatus.waitingNum()).isEqualTo(0L);
        assertThat(tokenStatus.progress()).isEqualTo(ProgressStatus.WAITING);
    }

    @Test
    @DisplayName("토큰 상태 확인 요청 시 ACTIVE 상태를 반환한다.")
    void givenToken_whenCheckTokenStatus_thenExistTokenInActiveQueue() {
        // given
        WaitingQueueInfo generateToken = waitingQueueFacade.generateToken();
        redisTemplate.opsForZSet().remove(waitingQueueKey, generateToken.token());
        redisTemplate.opsForSet().add(activeQueueKey, generateToken.token());

        // when
        WaitingQueueInfo tokenStatus = waitingQueueFacade.getTokenStatus(generateToken.token());

        // then
        assertThat(tokenStatus).isNotNull();
        assertThat(tokenStatus.token()).isEqualTo(generateToken.token());
        assertThat(tokenStatus.waitingNum()).isEqualTo(0L);
        assertThat(tokenStatus.progress()).isEqualTo(ProgressStatus.ACTIVE);
    }

    @Test
    @DisplayName("토큰 상태 확인 요청시 토큰이 존재하지 않아 CommonException이 발생한다.")
    void givenToken_whenCheckTokenStatus_thenNotExistTokenAndThrowCommonException() {
        // given
        WaitingQueueInfo generateToken = waitingQueueFacade.generateToken();
        redisTemplate.opsForZSet().remove(waitingQueueKey, generateToken.token());

        // when & then
        Assertions.assertThrows(CommonException.class, () -> waitingQueueFacade.getTokenStatus(generateToken.token()));
    }


}
