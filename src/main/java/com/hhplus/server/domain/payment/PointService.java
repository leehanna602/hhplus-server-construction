package com.hhplus.server.domain.payment;

import com.hhplus.server.domain.support.exception.PaymentErrorCode;
import com.hhplus.server.domain.payment.model.Point;
import com.hhplus.server.domain.payment.model.PointHistory;
import com.hhplus.server.domain.payment.model.TransactionType;
import com.hhplus.server.domain.support.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
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

    // 비관적락
    @Transactional
    public Point pointUseTransaction(Long userId, int amount, TransactionType type) {
        try {
            Point point = pointWriter.findByUserPointWithPessimisticLock(userId);
            return pointTransaction(point, amount, type);
        } catch (PessimisticLockingFailureException e) {
            throw new CommonException(PaymentErrorCode.POINT_USE_LOCK_ACQUISITION_FAILURE);
        } catch (Exception e) {
            throw new CommonException(PaymentErrorCode.POINT_USE_FAILURE);
        }
    }

    // 낙관적락
    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 1000),
            recover = "recoverPointCharge"
    )
    @Transactional
    public Point pointChargeTransaction(Long userId, int amount, TransactionType type) {
        try {
            Point point = pointWriter.findByUserPointWithOptimisticLock(userId);
            return pointTransaction(point, amount, type);
        } catch (OptimisticLockingFailureException e) {
            throw e;
        } catch (Exception e) {
            throw new CommonException(PaymentErrorCode.POINT_CHARGE_FAILURE);
        }
    }

    @Recover
    public Point recoverPointCharge(OptimisticLockingFailureException e, Long userId, int amount, TransactionType type) {
        log.error("포인트 충전 최대 재시도 실패. userId={}, amount={}", userId, amount);
        throw new CommonException(PaymentErrorCode.POINT_CHARGE_LOCK_ACQUISITION_FAILURE);
    }

    private Point pointTransaction(Point point, int amount, TransactionType type) {
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
