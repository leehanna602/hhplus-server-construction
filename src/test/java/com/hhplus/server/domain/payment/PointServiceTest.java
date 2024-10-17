package com.hhplus.server.domain.payment;

import com.hhplus.server.domain.payment.model.Point;
import com.hhplus.server.domain.payment.model.PointHistory;
import com.hhplus.server.domain.payment.model.TransactionType;
import com.hhplus.server.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private PointReader pointReader;

    @Mock
    private PointWriter pointWriter;

    @InjectMocks
    private PointService pointService;

    @Test
    void 포인트_충전_성공후_히스토리저장() {
        // given
        User user = new User(1L, "user1");
        int amount = 10000;
        TransactionType type = TransactionType.CHARGE;
        Point beforePoint = new Point(15L, user, 15000);
        when(pointWriter.findByUserPointWithLock(user.getUserId())).thenReturn(beforePoint);

        Point afterPoint = new Point(15L, user, 15000 + amount);
        when(pointWriter.save(any(Point.class))).thenReturn(afterPoint);

        PointHistory pointHistory = new PointHistory(1L, afterPoint, amount, afterPoint.getPoint(), type);
        when(pointWriter.saveHistory(any(PointHistory.class))).thenReturn(pointHistory);

        // when
        Point point = pointService.pointTransaction(user.getUserId(), amount, type);

        // then
        assertNotNull(point);
        assertEquals(point.getPoint(), afterPoint.getPoint());
        assertEquals(pointHistory.getPoint(), afterPoint);
    }

}