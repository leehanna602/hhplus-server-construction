package com.hhplus.server.domain.payment;

import com.hhplus.server.domain.payment.model.Point;
import com.hhplus.server.domain.payment.model.PointHistory;

public interface PointWriter {

    Point findByUserPointWithOptimisticLock(Long userId);

    Point findByUserPointWithPessimisticLock(Long userId);

    Point save(Point point);

    PointHistory saveHistory(PointHistory pointHistory);
}
