package com.hhplus.server.domain.token;

import com.hhplus.server.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "waiting_queue")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WaitingQueue {
    @Id
    @Column(name = "queue_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queueId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "progress")
    private ProgressStatus progress;

    @Column(name = "waiting_num")
    private Long waitingNum;

    @Column(name = "token_create_dt")
    private LocalDateTime tokenCreateDt;

    @Column(name = "token_expired_dt")
    private LocalDateTime tokenExpiredDt;

    @Column(name = "token_update_dt")
    private LocalDateTime tokenUpdateDt;

}
