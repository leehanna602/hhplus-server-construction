package com.hhplus.server.domain.concert;

import com.hhplus.server.domain.common.exception.ConcertErrorCode;
import com.hhplus.server.domain.concert.model.ConcertSeat;
import com.hhplus.server.domain.support.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcertReservationDistributedLockService {

    private final ConcertReader concertReader;
    private final ConcertWriter concertWriter;
    private final RedissonClient redissonClient;

    /* 분산락 */
    @Transactional
    public ConcertSeat findConcertSeatForReservationWithDistributedLock(Long concertId, Long scheduleId, Long seatId) {
        ConcertSeat concertSeat = null;

        RLock lock = redissonClient.getLock("reserv:" + concertId + scheduleId + seatId);
        try {
            if (!lock.tryLock(5, 3, TimeUnit.SECONDS)) {
                throw new CommonException(ConcertErrorCode.RESERVATION_LOCK_DISTRIBUTED_FAILURE);
            }
            log.info("get lock: {}", lock.getName());
            concertSeat = concertReader.findConcertSeatForReservationWithOptimisticLock(concertId, scheduleId, seatId)
                    .orElseThrow(() -> new CommonException(ConcertErrorCode.INVALID_SEAT));

            concertSeat.temporaryReserved();
            concertSeat = concertWriter.save(concertSeat);

        } catch (InterruptedException e) {
            throw new CommonException(ConcertErrorCode.RESERVATION_SYSTEM_ERROR);
        }finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return concertSeat;
    }


}
