# GDS-System 리팩토링 및 아키텍처 개선 가이드
**목표:** 파일/메모리 기반의 모놀리식 구조를 RDBMS 연동 및 향후 네트워크(C/S) 확장이 가능한 **계층형 아키텍처(Layered Architecture)**로 전환한다.

---

## 1. 도메인(Domain) 모델 재설계 및 고도화
각 도메인 객체는 수동적인 데이터 덩어리가 아닌, 스스로 비즈니스 규칙을 검증하고 행동하는 객체지향적인 형태를 갖춰야 한다.

### 1.1. User 계열 (`User`, `MemberGamer`, `Developer`, `Administrator`)
* **식별자 분리:** 로그인 ID와 시스템 내부 고유 식별자(PK/UID)를 분리.
* **가변성 부여:** DB 업데이트(부서 이동, 이름 변경 등)를 위해 `final` 키워드 제거 및 의미 있는 수정 메서드(`updateDepartment`, `changePassword`) 추가.
* **보안:** `checkPassword()` 로직을 단방향 해시 검증 방식으로 전환 고려.
* **통합 매핑:** 향후 DB 설계 시 단일 `users` 테이블에 넣고 `UserType`으로 구분하는 전략 염두.

### 1.2. Game 계열 (`Game`, `GameFile`)
* **장르 분리:** `String genre`를 `List<Genre>`로 변경하여 N:M 테이블 구조 대응.
* **패치 대응:** 게임 파일 업데이트, 가격 변동 등을 위해 가변 필드로 전환 (`final` 해제).
* **객체 생성:** 파라미터가 많은 객체의 안전한 생성을 위해 **Builder 패턴** 도입.

### 1.3. 상태 및 상수 (`DistributionStatus`, `GConstant`)
* **Enum 전환:** 하드코딩된 String 상수(유저 타입, 배포 상태 등)를 모두 **`Enum(열거형)`**으로 전환하여 타입 안전성 확보 및 DB 용량 최적화(코드값 매핑) 구현.

### 1.4. 장바구니 및 라이브러리 (`Cart`, `Library`)
* **상태 유지 객체:** 클래스 내부에 DB 통신 로직을 배제하고, `calculateTotalPrice()`, `hasGame()` 등 순수 도메인 로직만 캡슐화.
* **N:M 매핑:** DB 테이블 설계 시 단독 테이블이 아닌, `User`와 `Game`을 잇는 매핑 테이블(`cart_items`, `library_items`)로 구현.

### 1.5. 결제 및 주문 (`Purchase`, `Payment`)
* **역할 분리:** * `Purchase`: '누가, 무엇을, 총 얼마에 사려하는가' (주문서)
  * `Payment`: '어떤 수단으로 결제 트랜잭션을 발생시켰는가' (영수증)
* **1:N 구조:** 하나의 주문(Purchase)에 복합 결제(Payment)가 가능하도록 관계 분리.
* **상태 필드:** PENDING, COMPLETED, REFUNDED 등 상태값을 Enum으로 관리.

---

## 2. 계층 분리 4단계 로드맵 (Migration Steps)

### Step 1: 규격과 껍데기 설계 (영향도 0%)
* **DAO 인터페이스 생성:** `UserDAO`, `GameDAO`, `CartDAO` 등 DB 통신 규격 정의.
* **DTO 클래스 생성:** UI 및 클라이언트와의 통신을 위한 포장지(`GameResponseDTO`, `CartDTO` 등) 생성. (도메인 객체 직접 노출 방지)

### Step 2: Service 계층 (바리스타) 도입 (영향도 0%)
* **비즈니스 로직 전담:** `AuthService`, `ShopService`, `OrderService` 생성.
* `Main`과 `DataStore`에 흩어져 있던 계산, 검증 로직을 Service 내부로 이관.
* Service는 오직 순수 도메인과 DAO 인터페이스만 조작(Stateless 유지).

### Step 3: 데이터베이스 연동 (창고지기)
* **RDBMS 구성:** MySQL/Oracle 등에 실제 테이블 구성 (매핑 테이블 포함).
* **DAO 구현체 작성:** `JdbcGameDAO` 등 인터페이스 구현체를 만들어 실제 SQL(`SELECT`, `INSERT`) 및 `PreparedStatement` 로직 작성.

### Step 4: 레거시 청산 (스위치 교체)
* **`DataStore` 완전 해체:** 메모리에 데이터를 끌어안고 있던 God Object 삭제.
* **`Shop` 클래스 분해:** UI(`System.out`)는 Main으로, 필터링은 DAO로, 제어 흐름은 `ShopService`로 완전히 찢어서 분리.
* **`Main`의 역할 축소 (Controller 전환):** `Main`은 향후 도입될 네트워크 스켈레톤(통신 모듈)의 접수처 역할만 하도록, 직접적인 DB/로직 호출을 끊고 Service만 호출하도록 의존성 재조립.