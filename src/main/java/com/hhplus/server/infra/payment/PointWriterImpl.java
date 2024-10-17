package com.hhplus.server.infra.payment;

import com.hhplus.server.domain.payment.PointWriter;
import com.hhplus.server.domain.payment.model.Point;
import com.hhplus.server.domain.payment.model.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointWriterImpl implements PointWriter {

    private final PointJpaRepository pointJpaRepository;
    private final PointHistoryJpaRepository pointHistoryJpaRepository;

    @Override
    public Point findByUserPointWithLock(Long userId) {
        return pointJpaRepository.findByUserPointWithLock(userId);
    }

    @Override
    public Point save(Point point) {
        return pointJpaRepository.save(point);
    }

    @Override
    public PointHistory saveHistory(PointHistory pointHistory) {
        return pointHistoryJpaRepository.save(pointHistory);
    }
}
