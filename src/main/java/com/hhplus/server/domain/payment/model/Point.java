package com.hhplus.server.domain.payment.model;

import com.hhplus.server.domain.support.exception.CommonException;
import com.hhplus.server.domain.support.exception.CommonErrorCode;
import com.hhplus.server.domain.support.exception.PaymentErrorCode;
import com.hhplus.server.domain.base.BaseEntity;
import com.hhplus.server.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "point",
        indexes = {
                @Index(name = "point_user_id_IDX", columnList = "user_id")
        }
)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Version
    private Long version;

    private static final long maxPoint = 1000000;

    public void validateTransaction(long currentAmount, long amount, TransactionType type) {
        switch (type) {
            case CHARGE:
                if (currentAmount + amount > maxPoint) {
                    throw new CommonException(PaymentErrorCode.EXCEEDED_CHARGE_AMOUNT);
                }
                break;
            case USE:
                if (currentAmount < amount) {
                    throw new CommonException(PaymentErrorCode.INSUFFICIENT_POINT);
                }
                break;
            default:
                throw new CommonException(CommonErrorCode.INVALID_INPUT_VALUE);
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
