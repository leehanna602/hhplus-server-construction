package com.hhplus.server.domain.waitingQueue.model;

import com.hhplus.server.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "waiting_queue")
@Getter
@AllArgsConstructor
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
        if (progress == ProgressStatus.EXPIRED) {
            throw new RuntimeException("만료된 토큰입니다: " + token);
        }
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("만료 시간이 지난 토큰 입니다: " + token);
        }
    }

    public void waitingToActiveToken() {
        this.progress = ProgressStatus.ACTIVE;
    }

    public void expireToken() {
        this.progress = ProgressStatus.EXPIRED;
    }

}

