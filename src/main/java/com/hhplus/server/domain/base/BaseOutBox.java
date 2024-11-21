package com.hhplus.server.domain.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseOutBox extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "payload", columnDefinition = "json")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "outbox_status")
    private OutBoxStatus outboxStatus;

    @Version
    private Long version;

    public BaseOutBox(String payload) {
        super();
        this.payload = payload;
        this.outboxStatus = OutBoxStatus.INIT;
        this.version = 0L;
    }

    public void outBoxPublish() {
        this.outboxStatus = OutBoxStatus.PUBLISH;
    }

}
