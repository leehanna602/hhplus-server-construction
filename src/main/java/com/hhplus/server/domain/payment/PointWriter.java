package com.hhplus.server.domain.payment;

import com.hhplus.server.domain.payment.model.Point;
import com.hhplus.server.domain.payment.model.PointHistory;
import com.hhplus.server.domain.payment.model.TransactionType;

public interface PointWriter {
    Point findByUserPointWithLock(Long userId);

    Point save(Point point);

    PointHistory saveHistory(PointHistory pointHistory);
}
