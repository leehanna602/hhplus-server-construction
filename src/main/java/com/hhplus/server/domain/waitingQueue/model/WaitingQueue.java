package com.hhplus.server.domain.waitingQueue.model;

import com.hhplus.server.domain.support.exception.CommonException;
import com.hhplus.server.domain.common.exception.WaitingQueueErrorCode;
import com.hhplus.server.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "waiting_queue")
@Getter
@AllArgsConstructor
@Builder
public class WaitingQueue extends BaseEntity {
    @Id
    @Column(name = "queue_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queueId;

    @Column(name = "token")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "progress")
    private ProgressStatus progress;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    public WaitingQueue() {
        this.token = String.valueOf(UUID.randomUUID());
        this.progress = ProgressStatus.WAITING;
        this.expiredAt = LocalDateTime.now().plusMinutes(10L);
    }

    public void validateToken() {
        if (progress == ProgressStatus.EXPIRED || expiredAt.isBefore(LocalDateTime.now())) {
            throw new CommonException(WaitingQueueErrorCode.EXPIRED_TOKEN);
        }
    }

    public void waitingToActiveToken() {
        this.progress = ProgressStatus.ACTIVE;
    }

    public void expireToken() {
        this.progress = ProgressStatus.EXPIRED;
    }

}

