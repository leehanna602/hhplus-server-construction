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

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointTransactionIntegrationTest {

    @Autowired
    private PointFacade pointFacade;
    @Autowired
    private UserService userService;

    private User testUser;
    private Point point;

    private static final int USER_DEFAULT_AMOUNT = 100000;

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
                .point(USER_DEFAULT_AMOUNT)
                .version(1L)
                .build();
        point = pointService.save(point);
    }

    @Test
    @DisplayName("동일한 사용자가 동시에 여러건의 포인트 충전 요청시 낙관적락 동시성 처리로 인해 대부분 실패")
    void concurrentPointChargeWithOptimisticLockFailure() throws InterruptedException {
        int THREAD_COUNT = 100;
        int CHARGE_AMOUNT = 1000;

        // given
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // when
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    UserPointInfo userPointInfo = pointFacade.pointTransaction(testUser.getUserId(), CHARGE_AMOUNT, TransactionType.CHARGE);
                    System.out.println(userPointInfo);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        // 마지막 포인트 조회
        UserPointInfo finalPoint = pointFacade.getUserPointInfo(testUser.getUserId());

        // 예상 총 포인트 = 유저 초기 포인트 + (성공한 개수 * 충전금액)
        System.out.println("최종 유저 포인트: " + finalPoint);
        int expectedTotalPoint = USER_DEFAULT_AMOUNT + (CHARGE_AMOUNT * successCount.get());
        assertThat(finalPoint.point()).isEqualTo(expectedTotalPoint);
    }


    @Test
    @DisplayName("동일한 사용자가 동시에 100건의 포인트 사용 요청시 비관적락 동시성 처리로 모두 성공")
    void concurrentPointUseWithPessimisticLockSuccess() throws InterruptedException {
        long beforeTime = System.currentTimeMillis();

        // given
        int THREAD_COUNT = 100;
        int USE_AMOUNT = 500;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // when
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    UserPointInfo userPointInfo = pointFacade.pointTransaction(testUser.getUserId(), USE_AMOUNT, TransactionType.USE);
                    System.out.println(userPointInfo);
                    return userPointInfo;
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long afterTime = System.currentTimeMillis();
        long diffTime = afterTime - beforeTime;
        System.out.println("실행 시간(ms): " + diffTime + "ms");

        // then
        // 마지막 포인트 조회
        UserPointInfo finalPoint = pointFacade.getUserPointInfo(testUser.getUserId());

        // 예상 총 포인트 = 유저 초기 포인트 - (충전금액 * 스레드 수)
        int expectedTotalPoint = USER_DEFAULT_AMOUNT - (USE_AMOUNT * THREAD_COUNT);
        assertThat(finalPoint.point()).isEqualTo(expectedTotalPoint);
        assertThat(finalPoint.point()).isEqualTo(50000);
    }

    /* 테스트용 : 비관적락과 비교하기 위한 테스트 */
    @Test
    @DisplayName("동일한 사용자가 동시에 100건의 포인트 사용 요청시 낙관적락 동시성 처리로 대부분 실패")
    void concurrentPointUseWithOptimisticLockFailure() throws InterruptedException {
        long beforeTime = System.currentTimeMillis();

        // given
        int THREAD_COUNT = 100;
        int USE_AMOUNT = 500;
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // when
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    UserPointInfo userPointInfo = pointFacade.pointTransaction(testUser.getUserId(), USE_AMOUNT, TransactionType.USE);
                    System.out.println(userPointInfo);
                    return userPointInfo;
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long afterTime = System.currentTimeMillis();
        long diffTime = afterTime - beforeTime;
        System.out.println("실행 시간(ms): " + diffTime + "ms");

        // then
        // 마지막 포인트 조회
        UserPointInfo finalPoint = pointFacade.getUserPointInfo(testUser.getUserId());

        // 예상 총 포인트 = 유저 포인트 - (충전금액 * 스레드 수)
        int expectedTotalPoint = USER_DEFAULT_AMOUNT - (USE_AMOUNT * THREAD_COUNT);
        assertThat(finalPoint.point()).isGreaterThan(expectedTotalPoint);
        assertThat(finalPoint.point()).isGreaterThan(50000);
    }


}
