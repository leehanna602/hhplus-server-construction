package com.hhplus.server.domain.payment.model;

import com.hhplus.server.domain.base.BaseEntity;
import com.hhplus.server.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "point")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Point extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long pointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "point")
    private int point;

    private static final long maxPoint = 1000000;

    public void validateTransaction(long currentAmount, long amount, TransactionType type) {
        switch (type) {
            case CHARGE:
                if (currentAmount + amount > maxPoint) {
                    throw new IllegalArgumentException("충전 가능한 총액을 초과하였습니다.");
                }
                break;
            case USE:
                if (currentAmount < amount) {
                    throw new IllegalArgumentException("사용 가능한 포인트가 부족합니다.");
                }
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 타입입니다.");
        }
    }

    public void calculateNewAmount(long currentAmount, long amount, TransactionType type) {
        switch (type) {
            case CHARGE:
                this.point = (int) (currentAmount + amount);
                break;
            case USE:
                this.point = (int) (currentAmount - amount);
                break;
        };
    }
}
