package com.hhplus.server.domain.payment;

import com.hhplus.server.domain.payment.model.Point;
import com.hhplus.server.domain.payment.model.PointHistory;
import com.hhplus.server.domain.payment.model.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointService {

    private final PointReader pointReader;
    private final PointWriter pointWriter;

    @Transactional(readOnly = true)
    public Point getUserPointInfo(Long userId) {
        return pointReader.findByUserId(userId);
    }

    @Transactional
    public Point pointTransaction(Long userId, int amount, TransactionType type) {
        Point point = pointWriter.findByUserPointWithLock(userId);
        point.validateTransaction(point.getPoint(), amount, type);
        point.calculateNewAmount(point.getPoint(), amount, type);

        point = pointWriter.save(point);
        PointHistory pointHistory = new PointHistory(point, amount, type);
        pointWriter.saveHistory(pointHistory);

        return point;
    }

    public Point save(Point point) {
        return pointWriter.save(point);
    }

}
