package com.hhplus.server.domain.payment.model;

import com.hhplus.server.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(
        name = "point_history",
        indexes = {
                @Index(name = "point_history_point_id_IDX", columnList = "point_id")
        }
)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_history_id")
    private Long pointHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id")
    private Point point;

    @Column(name = "amount")
    private int amount;

    @Column(name = "point_after_transaction")
    private int pointAfterTransaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    public PointHistory(Point point, int amount, TransactionType type) {
        this.point = point;
        this.amount = amount;
        this.pointAfterTransaction = point.getPoint();
        this.type = type;
    }
}