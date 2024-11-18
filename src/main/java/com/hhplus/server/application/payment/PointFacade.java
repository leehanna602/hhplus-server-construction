package com.hhplus.server.application.payment;

import com.hhplus.server.domain.support.exception.CommonException;
import com.hhplus.server.domain.support.exception.PaymentErrorCode;
import com.hhplus.server.domain.payment.PointService;
import com.hhplus.server.domain.payment.dto.UserPointInfo;
import com.hhplus.server.domain.payment.model.Point;
import com.hhplus.server.domain.payment.model.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PointFacade {

    private final PointService pointService;

    public UserPointInfo getUserPointInfo(Long userId) {
        Point point = pointService.getUserPointInfo(userId);
        if (point == null) {
            throw new CommonException(PaymentErrorCode.INSUFFICIENT_POINT);
        }
        return new UserPointInfo(point.getUser().getUserId(), point.getPoint());
    }

    public UserPointInfo pointTransaction(Long userId, int amount, TransactionType type) {
        Point point = null;
        if (type.equals(TransactionType.CHARGE)) {
            point = pointService.pointChargeTransaction(userId, amount, type);

        }
        if (type.equals(TransactionType.USE)) {
            point = pointService.pointUseTransaction(userId, amount, type);
        }
        return new UserPointInfo(point.getUser().getUserId(), point.getPoint());
    }

}
