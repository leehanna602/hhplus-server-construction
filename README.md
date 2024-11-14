# ️🎪️콘서트 예약 서비스
![Java](https://img.shields.io/badge/Java-17-red)
![Spring-boot](https://img.shields.io/badge/Spring--boot-3.3.4-brightgreen)

## ✅ Description
- `콘서트 예약 서비스`를 구현해 봅니다.
- 대기열 시스템을 구축하고, 예약 서비스는 작업가능한 유저만 수행할 수 있도록 해야합니다.
- 사용자는 좌석예약 시에 미리 충전한 잔액을 이용합니다.
- 좌석 예약 요청시에, 결제가 이루어지지 않더라도 일정 시간동안 다른 유저가 해당 좌석에 접근할 수 없도록 합니다.

## ✅ Requirements
* [Requirements.md](docs/Requirements.md)

## ✅ Step Information
* [StepInfo.md](docs%2FStepInfo.md)

## ✅ 설계
### **`Milestone`**
* [Milestone.md](docs/Milestone.md)

### **`Sequence Diagram`**
- [SequenceDiagram.md](docs/SequenceDiagram.md)

### **`ERD`**
* [ERD.md](docs/ERD.md)

### **`API Spec`**
* [ApiSpecification.md](docs/ApiSpecification.md)

### **`Architecture`**
* Clean Architecture + Layered Architecture

## ✅ API Swagger
* http://localhost:8080/swagger-ui/index.html <br>
<img src="/docs/swagger.png" width="500px" title="swagger"/>

## ✅ 회고록
* [memoirs.md](docs%2Fmemoirs.md)

## ✅ 성능 개선
### 📒 동시성 제어 방식
* [ConcurrencyControl.md](docs%2FConcurrencyControl%2FConcurrencyControl.md)
### 📒 Redis를 이용한 성능 개선
* [RedisLogic.md](docs%2FRedis%2FRedisLogic.md)
### 📒 DB Index 설정 및 쿼리 성능 개선
* [Index.md](docs%2FIndex%2FIndex.md)
### 📒 Transaction 분리를 통한 개선
* [Transaction.md](docs%2FTransaction%2FTransaction.md)
