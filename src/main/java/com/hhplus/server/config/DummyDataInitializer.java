//package com.hhplus.server.config;
//
//import com.hhplus.server.domain.concert.model.Concert;
//import com.hhplus.server.domain.concert.model.ConcertSchedule;
//import com.hhplus.server.domain.concert.model.ConcertSeat;
//import com.hhplus.server.domain.concert.model.SeatStatus;
//import com.hhplus.server.domain.user.model.User;
//import com.hhplus.server.infra.concert.ConcertJpaRepository;
//import com.hhplus.server.infra.concert.ConcertScheduleJpaRepository;
//import com.hhplus.server.infra.concert.ConcertSeatJpaRepository;
//import com.hhplus.server.infra.user.UserJpaRepository;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.LocalDateTime;
//import java.util.Random;
//
//@Configuration
//public class DummyInitializer implements ApplicationRunner {
//
//
//    private final UserJpaRepository userJpaRepository;
//    private final ConcertJpaRepository concertJpaRepository;
//    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
//    private final ConcertSeatJpaRepository concertSeatJpaRepository;
//
//    public DummyInitializer(UserJpaRepository userJpaRepository, ConcertJpaRepository concertJpaRepository, ConcertScheduleJpaRepository concertScheduleJpaRepository, ConcertSeatJpaRepository concertSeatJpaRepository) {
//        this.userJpaRepository = userJpaRepository;
//        this.concertJpaRepository = concertJpaRepository;
//        this.concertScheduleJpaRepository = concertScheduleJpaRepository;
//        this.concertSeatJpaRepository = concertSeatJpaRepository;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//
//        // 10만개의 데이터 생성
//
//        // user
//        for (long i = 1; i < 101; i++) {
//            User user = new User(i, "user" + i);
//            userJpaRepository.save(user);
//        }
//
//        // concert
//        for (long i = 1; i < 1001; i++) {
//            Concert concert = new Concert(i, "concert " + i);
//            concertJpaRepository.save(concert);
//        }
//
//        // concertSchedule
//        long cnt = 1;
//        Random rd = new Random();
//        for (long i = 1; i < 1001; i++) {
//            int rdIntRange1 = rd.nextInt(1, 30);
//            int rdIntRange2 = rd.nextInt(10, 20);
//            ConcertSchedule concertSchedule1 = new ConcertSchedule(cnt,
//                    new Concert(i, "concert" + i),
//                    LocalDateTime.of(2024, 12, rdIntRange1, rdIntRange2, 0),
//                    50);
//            concertScheduleJpaRepository.save(concertSchedule1);
//            cnt = cnt + 1;
//
//            int rdIntRange3 = rd.nextInt(1, 30);
//            int rdIntRange4 = rd.nextInt(10, 20);
//            ConcertSchedule concertSchedule2 = new ConcertSchedule(cnt,
//                    new Concert(i, "concert" + i),
//                    LocalDateTime.of(2024, 12, rdIntRange3, rdIntRange4, 0),
//                    50);
//            concertScheduleJpaRepository.save(concertSchedule2);
//            cnt = cnt + 1;
//        }
//
//        // concertSeat
//        long concertScheduleId = 1;  // 1~2000까지 사용됨
//
//        // 1000개의 콘서트 생성
//        for (long concertId = 1; concertId <= 1000; concertId++) {
//            // 콘서트 생성
//            Concert concert = new Concert(concertId, "concert" + concertId);
//
//            // 각 콘서트마다 2개의 스케줄 생성
//            for (int scheduleCount = 1; scheduleCount <= 2; scheduleCount++) {
//                // 콘서트 스케줄 생성
//                ConcertSchedule concertSchedule = new ConcertSchedule(
//                        concertScheduleId,
//                        concert,
//                        LocalDateTime.now().plusDays(scheduleCount),  // 날짜를 다르게 설정
//                        50
//                );
//
//                // 각 스케줄마다 50개의 좌석 생성
//                for (int seatNum = 1; seatNum <= 50; seatNum++) {
//                    int rdPrice = rd.nextInt(10000, 50000);
//                    SeatStatus status = SeatStatus.values()[rd.nextInt(SeatStatus.values().length)];
//
//                    ConcertSeat concertSeat = new ConcertSeat(
//                            null,  // ID는 자동생성
//                            concert,
//                            concertSchedule,
//                            seatNum,
//                            rdPrice,
//                            status,
//                            1L
//                    );
//
//                    concertSeatJpaRepository.save(concertSeat);
//                }
//
//                concertScheduleId++;  // 다음 스케줄 ID로 증가
//            }
//        }
//
//    }
//
//}
