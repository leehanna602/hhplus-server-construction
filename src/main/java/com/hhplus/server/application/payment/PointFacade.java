package com.hhplus.server.application.payment;

import com.hhplus.server.domain.payment.PointService;
import com.hhplus.server.domain.payment.dto.UserPointInfo;
import com.hhplus.server.domain.payment.model.Point;
import com.hhplus.server.domain.payment.model.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class PointFacade {

    private final PointService pointService;

    public UserPointInfo getUserPointInfo(Long userId) {
        Point point = pointService.getUserPointInfo(userId);
        if (point == null) {
            throw new RuntimeException("포인트가 존재하지 않습니다.");
        }
        return new UserPointInfo(point.getUser().getUserId(), point.getPoint());
    }

    @Transactional
    public UserPointInfo chargePoint(Long userId, int amount, TransactionType type) {
        Point point = pointService.pointTransaction(userId, amount, type);
        return new UserPointInfo(point.getUser().getUserId(), point.getPoint());
    }

}
