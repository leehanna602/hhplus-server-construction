## 😺 Transaction 분리를 통한 개선
> 목차
> - Transaction 분리의 필요성
> - Transaction 분리로 발생할 수 있는 문제와 해결방안
> - Transaction 분리1. 좌석예약 
> - Transaction 분리2. 결제 

### 📒 Transaction 분리의 필요성
Transaction의 범위에 따라 다음과 같이 성능에 영향을 줄 수 있습니다. 따라서 Transaction을 분리할 필요가 있습니다.

1. Transaction 범위가 불필요하게 많거나 느린 쿼리가 포함되어 있는 경우
   - 트랜잭션 내에서 Lock을 갖고 있다면 다른 사용자는 트랜잭션이 끝날때까지 대기해야합니다. 그리고 데드락이 발생할 수도 있습니다.
   - 다수의 느린 쿼리가 포함된 작업은 요청 처리에 영향을 줄 수 있습니다.
   - 긴 생명 주기의 Transaction 의 경우, 오랜 시간은 소요되나 후속 작업에 의해 전체 트랜잭션이 실패할 수 있습니다.

<br>

2. Transaction 범위 내에서 DB 와 무관한 작업이 포함된 경우
   - DB의 작업은 완료되거나 오래 걸리지 않는데 API 요청과 같은 DB 외적인 작업이 오래 걸리게 되면 Transaction 이 길어지는 문제가 있습니다.
   - 외부 API 호출에 실패할 경우 롤백됩니다. 그러나 이미 완료 되었기 때문에 롤백할 필요가 없습니다.
   - 예를 들어, 좌석 예약, 결제 등이 완료 되었을 때 데이터 플랫폼에 전달하거나 이력을 저장하는 경우가 있습니다.

<br> 

이러한 케이스를 트랜잭션을 분리하고 Transactional Outbox Pattern을 적용하고 이벤트를 발행하도록하여 개선할 수 있으며, 다음과 같은 방법들을 사용할 수 있습니다.
- ApplicationEventPublisher & EventListner / TransactionEventListner
- Kafka 비동기 메시지 통신

<br>

### 📒 Transaction 분리로 발생할 수 있는 문제와 해결방안
1. 이벤트에 의한 작업이 실패가 영향을 미치는 경우
   - 만약 이벤트에 의한 작업이 실패할 경우 원본 작업도 실패 처리를 해야한다면 이를 위한 처리가 필요합니다.
   - 이를 위해 SAGA 패턴과 보상 트랜잭션을 통해 처리할 수 있으며, 이를 통해 분산환경에서도 처리가 가능합니다.

<br> 

2. 트랜잭션에서 이벤트 발행도록 적용한 후 이벤트 발행시 처리하는 로직이 실패할 경우
   - 이벤트 발행시 처리되는 로직이 실패할 경우, 만약 반드시 수행해야하는 로직이라면 이에 대한 처리가 필요합니다.

<br> 

### 📒 Transaction 분리1. 좌석예약
좌석예약의 경우 예약이 완료될 경우 알림톡 등을 전송할 수 있습니다. 이 경우 외부 API를 통해 예약 완료 메시지를 전달해야하므로 다음과 같이 개선할 수 있습니다.

* 기존 로직
  ```
  ReservationFacade {
    reservationService.분산락을_이용한_예약 ()
  }
  
  ReservationService {
    분산락을_이용한_예약 () {
      락_획득 ()
      concertReservationService.좌석_예약 ()
    } 
  }

  @Transactional
  ConcertReservationService {
    좌석_예약 () {
      1. seatId로 좌석 조회 (낙관적락)
      2. 좌석 상태 변경 저장 (AVAILABLE -> TEMPORARY_RESERVED)
      3. 예약 내역 저장 (Reservation)
    }
  }
  ```
  
* 개선 로직
  ```
  ReservationFacade {
    reservationService.분산락을_이용한_예약 ()
  }
  
  ReservationService {
    분산락을_이용한_예약 () {
      락_획득 ()
      concertReservationService.좌석_예약 ()
    } 
  }

  @Transactional
  ConcertReservationService {
    좌석_예약 () {
      1. 좌석_조회 (낙관적락)
      2. 좌석_상태변경 (AVAILABLE -> TEMPORARY_RESERVED)
      3. 예약내역_저장 (Reservation)
      4. 예약완료_이벤트발행 ()
    }
  }
  
  @Component
  ReservationEventPublisher {
    successReservation(ReservationInfo reservationInfo){
      applicationEventPublisher.publishEvent(reservationInfo);
    }
  }
  
  @Component
  ReservationEventListener {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    reservationSuccessHandler(ReservationInfo reservationInfo) {
      // 예약 완료 메시지 전달 외부 API 
    }
  }
  ```

* 로직 변경 후, 테스트 코드 실행 결과
  <img src="/docs/Transaction/reservation_test_1.png" width="500px" title="reservation_test_1"/>
  <img src="/docs/Transaction/reservation_test_2.png" width="500px" title="reservation_test_2"/>
  <img src="/docs/Transaction/reservation_test_3.png" width="500px" title="reservation_test_3"/>

<br> 

### 📒 Transaction 분리2. 결제
결제도 좌석 예약과 동일하게 결제가 완료된 후의 처리를 분리하여 다음과 같이 개선할 수 있습니다.
결제를 위한 기존 로직은 결제를 위한 여러 로직이 하나로 묶여있고, PaymentFacade에서 @Transactional을 사용하여 Transaction이 불필요하게 길어지는 문제가 생길 수 있어 이를 개선하였습니다.

* 기존 로직
  ```
  @Transactional 
  PaymentFacade {
    reservationService.예약정보_조회_및_검증 ()
    pointService.결제처리_비관적락_사용 ()
    paymentService.결제상태_완료처리 ()
    reservationService.예약상태_완료처리 ()
    concertService.좌석상태_완료처리 ()
    waitingQueueService.토큰_만료 ()
  }

  ReservationService {
    예약정보_조회_및_검증 ()
  
    @Transactional
    예약상태_완료처리 ()
  }

  PointService {
    @Transactional
    결제처리_비관적락_사용 () {
    }
  }

  PaymentService {
    @Transactional
    결제상태_완료처리 ()
  }

  ConcertService {
    @Transactional
      좌석상태_완료처리 ()
  }

  WaitingQueueService {
    토큰_만료 ()
  }
  ```

* 개선 로직
  ```
  PaymentFacade {
    paymentService.결제 ()
  }

  PaymentService {
    @Transactional
    결재() {
      reservationService.예약정보_조회_및_검증 ()
      pointService.결제처리_비관적락_사용 ()
  
      paymentService.결제상태_완료처리 ()
      reservationService.예약상태_완료처리 ()
      concertService.좌석상태_완료처리 ()
      
      결제완료_이벤트발행 ()
    
      waitingQueueService.토큰_만료 ()
    }
  
    @Transactional
    결제상태_완료처리 ()
  }

  @Component
  PaymentEventPublisher {
    successPayment(PaymentInfo paymentInfo){
      applicationEventPublisher.publishEvent(paymentInfo);
    }
  }
  
  @Component
  PaymentEventListener {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    paymentSuccessHandler(PaymentInfo paymentInfo) {
      // 결제 완료 메시지 전달 외부 API 
    }
  }
  
  ReservationService {
    예약정보_조회_및_검증 ()
  
    @Transactional
    예약상태_완료처리 ()
  }

  PointService {
    @Transactional
    결제처리_비관적락_사용 () {
    }
  }

  ConcertService {
    @Transactional
      좌석상태_완료처리 ()
  }

  WaitingQueueService {
    토큰_만료 ()
  }
  ```
  
* 로직 변경 후, 테스트 코드 실행 결과
  <img src="/docs/Transaction/payment_test_1.png" width="500px" title="payment_test_1"/>
  <img src="/docs/Transaction/payment_test_2.png" width="500px" title="payment_test_2"/>
