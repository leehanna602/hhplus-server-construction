package com.hhplus.server.domain.waitingQueue;

import com.hhplus.server.domain.waitingQueue.dto.WaitingQueueInfo;
import com.hhplus.server.domain.waitingQueue.model.ProgressStatus;
import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class WaitingQueueServiceTest {

    @Mock
    private WaitingQueueReader waitingQueueReader;

    @Mock
    private WaitingQueueWriter waitingQueueWriter;

    @InjectMocks
    private WaitingQueueService waitingQueueService;

    @Test
    void 대기열_토큰_존재_조회_성공() {
        // given
        WaitingQueue existingQueue = new WaitingQueue();
        String token = existingQueue.getToken();
        when(waitingQueueReader.findByToken(token)).thenReturn(Optional.of(existingQueue));

        // when
        WaitingQueueInfo waitingQueueInfo = waitingQueueService.getWaitingQueueInfo(token);

        // then
        assertNotNull(waitingQueueInfo);
        assertEquals(existingQueue.getToken(), waitingQueueInfo.token());
        verify(waitingQueueReader).findByToken(token);
    }

    @Test
    void 대기열_토큰_조회_실패_만료상태() {
        // given
        String token = UUID.randomUUID().toString();
        WaitingQueue existingQueue = new WaitingQueue(1L, token, ProgressStatus.EXPIRED, LocalDateTime.now().plusMinutes(1));
        when(waitingQueueReader.findByToken(token)).thenReturn(Optional.of(existingQueue));

        // when & then
        assertThrows(RuntimeException.class, () -> waitingQueueService.getWaitingQueueInfo(token));
    }

    @Test
    void 대기열_토큰_조회_실패_대기중_만료시간이후() {
        // given
        String token = UUID.randomUUID().toString();
        WaitingQueue existingQueue = new WaitingQueue(1L, token, ProgressStatus.WAITING, LocalDateTime.now().minusMinutes(10));
        when(waitingQueueReader.findByToken(token)).thenReturn(Optional.of(existingQueue));

        // when & then
        assertThrows(RuntimeException.class, () -> waitingQueueService.getWaitingQueueInfo(token));
    }

    @Test
    void 대기열_토큰_생성_성공() {
        // given
        when(waitingQueueReader.findByToken(null)).thenReturn(Optional.empty());
        WaitingQueue newWaitingQueue = new WaitingQueue(1L, UUID.randomUUID().toString(), ProgressStatus.WAITING, LocalDateTime.now().plusMinutes(10));
        when(waitingQueueWriter.save(any(WaitingQueue.class))).thenReturn(newWaitingQueue);

        // when
        WaitingQueueInfo waitingQueueInfo = waitingQueueService.getWaitingQueueInfo(null);

        // then
        assertEquals(newWaitingQueue.getToken(), waitingQueueInfo.token());
        verify(waitingQueueReader).findByToken(null);
        verify(waitingQueueWriter).save(any(WaitingQueue.class));
        verify(waitingQueueReader).getWaitingNum(newWaitingQueue.getQueueId());
    }

}