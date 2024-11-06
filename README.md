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

## ✅ STEP
### **`STEP 05`**
- 시나리오 선정 및 프로젝트 Milestone 제출
- 시나리오 요구사항 별 분석 자료 제출 (시퀀스 다이어그램, 플로우 차트 등)
- 자료들을 리드미에 작성 후 PR 링크 제출

### **`STEP 06`**
- ERD 설계 자료 제출
- API 명세 및 Mock API 작성
- 자료들을 리드미에 작성 후 PR링크 제출 ( 패키지 구조, 기술스택 등 )

### **`STEP 07`**
- API Swagger 기능 구현 및 캡쳐본 첨부 ( Readme )
- 주요 비즈니스 로직 개발 및 단위 테스트 작성

### **`STEP 08`**
- 비즈니스 Usecase 개발 및 통합 테스트 작성

### **`STEP 09`**
- 비즈니스 별 발생할 수 있는 에러 코드 정의 및 관리 체계 구축
- 프레임워크별 글로벌 에러 핸들러를 통해 예외 로깅 및 응답 처리 핸들러 구현
- 시스템 성격에 적합하게 Filter, Interceptor 를 활용해 기능의 관점을 분리하여 개선
- 모든 API 가 정상적으로 기능을 제공하도록 완성

### **`STEP 10`**
- 시나리오별 동시성 통합 테스트 작성
- Chapter 2 회고록 작성

### **`STEP 11`**
- 나의 시나리오에서 발생할 수 있는 동시성 이슈에 대해 파악하고 가능한 동시성 제어 방식들을 도입해보고 각각의 장단점을 파악한 내용을 정리 (구현의 복잡도, 성능, 효율성 등)

### **`STEP 12`**
- **DB Lock 을 활용한 동시성 제어 방식** 에서 해당 비즈니스 로직에서 적합하다고 판단하여 차용한 동시성 제어 방식을 구현하여 비즈니스 로직에 적용하고, 통합테스트 등으로 이를 검증

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

## ✅ 동시성 제어 방식
* [ConcurrencyControl.md](docs%2FConcurrencyControl%2FConcurrencyControl.md)