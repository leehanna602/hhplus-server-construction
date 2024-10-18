package com.hhplus.server.domain.payment;

import com.hhplus.server.domain.payment.model.Point;

public interface PointReader {
    Point findByUserId(Long userId);
}
