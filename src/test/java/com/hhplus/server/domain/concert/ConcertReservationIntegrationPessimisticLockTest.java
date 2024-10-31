package com.hhplus.server.domain.concert;

import com.hhplus.server.application.concert.ReservationFacade;
import com.hhplus.server.domain.concert.model.*;
import com.hhplus.server.domain.user.UserService;
import com.hhplus.server.domain.user.model.User;
import com.hhplus.server.domain.waitingQueue.WaitingQueueService;
import com.hhplus.server.domain.waitingQueue.WaitingQueueWriter;
import com.hhplus.server.domain.waitingQueue.model.ProgressStatus;
import com.hhplus.server.domain.waitingQueue.model.WaitingQueue;
import com.hhplus.server.infra.concert.ReservationReaderImpl;
import com.hhplus.server.interfaces.v1.concert.req.ReservationReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ConcertReservationIntegrationPessimisticLockTest {

    @Autowired
    private ReservationFacade reservationFacade;
    @Autowired
    private ConcertWriter concertWriter;
    @Autowired
    private WaitingQueueWriter waitingQueueWriter;
    @Autowired
    private ReservationReaderImpl reservationReaderImpl;
    @Autowired
    private UserService userService;
    @Autowired
    private WaitingQueueService waitingQueueService;

    private static final int THREAD_COUNT = 10;
    private Concert concert;
    private ConcertSchedule concertSchedule;
    private ConcertSeat concertSeat;
    private List<User> userList;
    private List<WaitingQueue> waitingQueueList;


    @BeforeEach
    void setUp() {
        userList = new ArrayList<>();
        waitingQueueList = new ArrayList<>();
        for (long i = 0; i < THREAD_COUNT; i++) {
            User user = User.builder()
                    .userId(i)
                    .userName("Test User " + (i + 1))
                    .build();
            userList.add(userService.save(user));

            WaitingQueue waitingQueue = WaitingQueue.builder()
                    .queueId(i)
                    .token("token" + (i + 1))
                    .progress(ProgressStatus.ACTIVE)
                    .expiredAt(LocalDateTime.now().plusMinutes(10))
                    .build();
            waitingQueueList.add(waitingQueueService.save(waitingQueue));
        }

        // 콘서트 생성
        concert = Concert.builder()
                .concertId(1L)
                .concertName("concert1")
                .build();
        concertWriter.save(concert);

        // 콘서트 일정 생성
        concertSchedule = ConcertSchedule.builder()
                .concertScheduleId(1L)
                .concert(concert)
                .concertDt(LocalDateTime.now().plusDays(2))
                .totalSeat(50)
                .build();
        concertWriter.save(concertSchedule);

        // 콘서트 좌석 생성
        concertSeat = ConcertSeat.builder()
                .seatId(1L)
                .concert(concert)
                .concertSchedule(concertSchedule)
                .seatNum(1)
                .price(5000)
                .seatStatus(SeatStatus.AVAILABLE)
                .version(1L)
                .build();
        concertWriter.save(concertSeat);
    }

    @Test
    void 콘서트예약_동시10개요청_예약한개_성공() throws InterruptedException {
        // given
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        AtomicInteger successCount = new AtomicInteger();
        List<Exception> exceptions = new ArrayList<>();

        // when
        for (int i = 0; i < THREAD_COUNT; i++) {
            User user = userList.get(i);
            WaitingQueue waitingQueue = waitingQueueList.get(i);
            executorService.submit(() -> {
                try {
                    ReservationReq request = new ReservationReq(
                            user.getUserId(),
                            waitingQueue.getToken(),
                            concert.getConcertId(),
                            concertSchedule.getConcertScheduleId(),
                            concertSeat.getSeatId()
                    );

                    reservationFacade.concertSeatReservationWithPessimisticLock(request);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    synchronized (exceptions) {
                        exceptions.add(e);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // then
        latch.await();
        executorService.shutdown();

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(exceptions).hasSize(THREAD_COUNT - 1);

        exceptions.forEach(exception ->
                assertThat(exception).isInstanceOf(Exception.class)
        );

    }

}
