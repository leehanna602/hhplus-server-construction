## 😺 콘서트 예약 서비스 API Spec
### `API 목록`
1. [유저 토큰 발급](#1-유저-토큰-발급)
2. [대기열 번호 조회](#2-대기열-번호-조회)
3. [예약 가능 날짜 조회](#3-예약-가능-날짜-조회)
4. [예약 가능 좌석 조회](#4-예약-가능-좌석-조회)
5. [좌석 예약](#5-좌석-예약)
6. [잔액 충전](#6-잔액-충전)
7. [잔액 조회](#7-잔액-조회)
8. [결제](#8-결제)

### `1. 유저 토큰 발급`
* Method: `POST`
* URL: `/v1/queue/token`
* Description: 유저 토큰 발급과 대기열 진입
* Request
  ```json
  {
    "userId": 1952
  }
  ```
* Response
  ```json
  {
    "userId": 1952,
    "token": "eyJhbGciOiJIUzI1...",
    "waitNum": 598,
    "waitTime": 300
  }
  ```

### `2. 대기열 번호 조회`
* Method: `POST`
* URL: `/v1/queue/status`
* Description: 유저 대기열 번호 조회
* Request
  ```json
  {
    "userId": 1952,
    "token": "eyJhbGciOiJIUzI1..."
  }
  ```
* Response
  ```json
  {
    "userId": 1952,
    "token": "eyJhbGciOiJIUzI1...",
    "waitNum": 598,
    "waitTime": 300
  }
  ```
  
### `3. 예약 가능 날짜 조회`
* Method: `GET`
* URL: `/v1/concert/scheduled/{concertId}`
* Description: 예약 가능한 날짜 조회
* Response
  ```json
  {
    "concertId": 586,
    "availableDates": [
      "2024-10-15", 
      "2024-10-16", 
      "2024-10-17"
    ]
  }
  ```
  
### `4. 예약 가능 좌석 조회`
* Method: `GET`
* URL: `/v1/concert/{concertId}/{date}/seats`
* Description: 예약 가능한 좌석 조회
* Response
  ```json
  {
    "concertId": 586,
    "scheduleId": 35,
    "availableSeatsCnt": 1,
    "availableSeats": [
      {
        "seatId": 1,
        "seatNum": 25,
        "price": 30000
      }
    ]
  }
  ```
  
### `5. 좌석 예약`
* Method: `POST`
* URL: `/v1/reservations/seat`
* Description: 콘서트 좌석 예약
* Request
  ```json
  {
    "userId": 1952,
    "token": "eyJhbGciOiJIUzI1...",
    "concertId": 586,
    "scheduleId": 35,
    "seatId": 1
  }
  ```
* Response
  ```json
  {
    "reservationId": 1233,
    "seatId": 1,
    "status": "TEMPORARY",
    "expiredDt": "2024-10-11T15:30:00Z"
  }
  ```
  
### `6. 잔액 충전`
* Method: `POST`
* URL: `/v1/point/charge`
* Description: 포인트 충전
* Request
  ```json
  {
    "userId": 1952,
    "type": "CHARGE",
    "amount": 25000
  }
  ```
* Response
  ```json
  {
    "userId": 1952,
    "point": 45000
  }
  ```
  
### `7. 잔액 조회`
* Method: `GET`
* URL: `/v1/point/{userId}`
* Description: 포인트 잔액 조회
* Response
  ```json
  {
    "userId": 1952,
    "point": 38000
  }
  ```
  
### `8. 결제`
* Method: `POST`
* URL: `/v1/payment`
* Description: 결제
* Request
  ```json
  {
    "userId": 1952,
    "token": "eyJhbGciOiJIUzI1...",
    "reservationId": 1233
  }
  ```
* Response
  ```json
  {
    "userId": 1952,
    "status": "COMPLETED"
  }
  ```