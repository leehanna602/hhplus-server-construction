package com.hhplus.server.domain.concert;

import com.hhplus.server.config.distributedLock.DistributedLock;
import com.hhplus.server.domain.support.exception.ConcertErrorCode;
import com.hhplus.server.domain.concert.dto.ReservationInfo;
import com.hhplus.server.domain.support.exception.CommonException;
import com.hhplus.server.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertReservationDistributedLockService {

    private final ConcertReservationService concertReservationService;
    private final RedissonClient redissonClient;

    /* 분산락 - 어노테이션 적용*/
    @DistributedLock(key = "'reserve:'.concat(#concertId).concat(':').concat(#scheduleId).concat(':').concat(#seatId)")
    public ReservationInfo findConcertSeatForReservationWithDistributedLock(User user, Long concertId, Long scheduleId, Long seatId) {

        // 어노테이션 적용 후, executeInReservationTransaction()의 로직을 여기에 구현해도 정상 동작
        ReservationInfo reservationInfo = concertReservationService.executeInReservationTransaction(user, seatId);

        return reservationInfo;
    }


    /* 분산락 - 어노테이션 미적용시 로직 */
    public ReservationInfo findConcertSeatForReservationWithDistributedLockNotUseAnnotation(User user, Long concertId, Long scheduleId, Long seatId) {
        ReservationInfo reservationInfo = null;

        RLock lock = redissonClient.getLock("reserv:" + concertId + ":" + scheduleId + ":" + seatId);
        try {
            boolean isLock = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (!isLock) {
                log.info("락을 얻지 못함");
                throw new CommonException(ConcertErrorCode.RESERVATION_LOCK_DISTRIBUTED_FAILURE);
            }
            log.info("get lock: {}", lock.getName());
            reservationInfo = concertReservationService.executeInReservationTransaction(user, seatId);

        } catch (InterruptedException e) {
            log.warn("get concert seat failed", e);
            throw new CommonException(ConcertErrorCode.RESERVATION_SYSTEM_ERROR);
        }finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("lock unlocked: {}", lock.getName());
            }
        }

        return reservationInfo;
    }


}
