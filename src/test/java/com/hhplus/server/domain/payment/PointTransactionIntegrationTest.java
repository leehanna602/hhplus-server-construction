package com.hhplus.server.domain.payment;

import com.hhplus.server.application.payment.PointFacade;
import com.hhplus.server.domain.payment.dto.UserPointInfo;
import com.hhplus.server.domain.payment.model.Point;
import com.hhplus.server.domain.payment.model.TransactionType;
import com.hhplus.server.domain.user.UserService;
import com.hhplus.server.domain.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointTransactionIntegrationTest {

    @Autowired
    private PointFacade pointFacade;
    @Autowired
    private UserService userService;

    private User testUser;
    private Point point;
    private static final int THREAD_COUNT = 10;
    private static final int CHARGE_AMOUNT = 1000;
    private static final int USE_AMOUNT = 500;
    @Autowired
    private PointService pointService;

    @BeforeEach
    void setUp() {
        // 테스트 사용자 생성
        testUser = User.builder()
                .userId(1L)
                .userName("Test User")
                .build();
        testUser = userService.save(testUser);

        point = Point.builder()
                .pointId(1L)
                .user(testUser)
                .point(0)
                .build();
        point = pointService.save(point);
    }

    @Test
    void 동시에_여러충전요청_포인트충전_정상() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        List<Future<UserPointInfo>> futures = new ArrayList<>();

        // when
        for (int i = 0; i < THREAD_COUNT; i++) {
            Future<UserPointInfo> future = executorService.submit(() -> {
                try {
                    return pointFacade.chargePoint(testUser.getUserId(), CHARGE_AMOUNT, TransactionType.CHARGE);
                } finally {
                    latch.countDown();
                }
            });
            futures.add(future);
        }

        latch.await();
        executorService.shutdown();

        // then
        // 마지막 포인트 조회
        UserPointInfo finalPoint = pointFacade.chargePoint(testUser.getUserId(), 0, TransactionType.CHARGE);

        // 예상 총 포인트 = 충전금액 * 스레드 수
        int expectedTotalPoint = CHARGE_AMOUNT * THREAD_COUNT;
        assertThat(finalPoint.point()).isEqualTo(expectedTotalPoint);
    }


}
