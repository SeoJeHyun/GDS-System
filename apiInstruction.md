# GDS Console Project API 명세서 (최신 백엔드 완벽 동기화)

## 🎮 1. 상점 및 게임 관련 API (ShopController)

### 1.1 게임 목록 전체 조회
- **Endpoint:** `GET /api/games`
- **특징:** 서버에 등록된 모든 게임의 목록을 반환합니다. 누구나 조회 가능합니다.
- **Response JSON:**
```json
{
  "status": "success",
  "message": "게임 목록 조회 성공",
  "data": [
    {
      "gameId": "game1",
      "title": "Game One",
      "developerName": "Sample Dev Studio",
      "price": 10000,
      "genre": "Action",
      "distributionStatus": "RELEASED"
    }
  ]
}
```

### 1.2 게임 검색
- **Endpoint:** `GET /api/games/search?keyword={keyword}`
- **특징:** 게임 제목에 검색어가 포함된 게임 목록을 반환합니다.
- **Response JSON:** (게임 목록 전체 조회와 동일한 규격)

### 1.3 게임 상세 정보 조회
- **Endpoint:** `GET /api/games/{gameId}`
- **특징:** 특정 게임의 상세 정보를 반환합니다.
- **Response JSON:**
```json
{
  "status": "success",
  "message": "게임 상세 조회 성공",
  "data": {
    "gameId": "game1",
    "title": "Game One",
    "developerName": "Sample Dev Studio",
    "price": 10000,
    "genre": "Action",
    "distributionStatus": "RELEASED"
  }
}
```

---

## 🛒 2. 장바구니 및 결제 관련 API (ShopController)

### 2.1 장바구니 목록 조회
- **Endpoint:** `GET /api/cart?userId={userId}`
- **특징:** 로그인한 사용자(userId)의 장바구니에 담긴 게임 목록을 조회합니다.
- **Response JSON:** (게임 목록 전체 조회와 동일한 규격)

### 2.2 장바구니 담기
- **Endpoint:** `POST /api/cart`
- **Request JSON:**
```json
{
  "userId": "user",
  "gameId": "game2"
}
```
- **Response JSON:**
```json
{
  "status": "success",
  "message": "장바구니에 게임이 추가되었습니다.",
  "data": {
    "gameId": "game2",
    "title": "Game Two",
    "developerName": "Sample Dev Studio",
    "price": 15000,
    "genre": "Adventure",
    "distributionStatus": "RELEASED"
  }
}
```

### 2.3 장바구니 특정 게임 삭제
- **Endpoint:** `DELETE /api/cart/{gameId}?userId={userId}`
- **특징:** 장바구니에서 특정 게임 하나를 삭제합니다. (주소와 쿼리 파라미터 활용)
- **Response JSON:**
```json
{
  "status": "success",
  "message": "장바구니에서 게임이 삭제되었습니다.",
  "data": {
    "id": "game2"
  }
}
```

### 2.4 장바구니 결제 확정 (토스페이먼츠 연동)
- **Endpoint:** `POST /api/purchase`
- **Request JSON:**
```json
{
  "userId": "user",
  "paymentKey": "toss_abc123",
  "orderId": "order_12345",
  "amount": 25000
}
```
- **Response JSON:**
```json
{
  "status": "success",
  "message": "결제 검증 및 구매 처리가 완료되었습니다.",
  "data": {
    "transactionId": "toss_abc123",
    "orderId": "order_12345",
    "totalPaidAmount": 25000
  }
}
```

---

## 👤 3. 유저 및 라이브러리 관련 API (UserController)

### 3.1 로그인
- **Endpoint:** `POST /api/login`
- **특징:** 유저 정보를 검증하고 성공 시 DTO를 반환합니다.
- **Request JSON:**
```json
{
  "userId": "user",
  "password": "user"
}
```
- **Response JSON:**
```json
{
  "status": "success",
  "message": "로그인 성공",
  "data": {
    "userId": "user",
    "name": "Sample User",
    "userType": "MEMBER_GAMER"
  }
}
```

### 3.2 회원가입
- **Endpoint:** `POST /api/register`
- **특징:** 일반 게이머 계정으로 회원가입을 진행합니다.
- **Request JSON:**
```json
{
  "userId": "user2",
  "password": "123",
  "name": "홍길동"
}
```
- **Response JSON:**
```json
{
  "status": "success",
  "message": "회원가입 성공",
  "data": {
    "userId": "user2",
    "name": "홍길동",
    "userType": "MEMBER_GAMER"
  }
}
```

### 3.3 라이브러리 (구매 목록) 조회
- **Endpoint:** `GET /api/users/{userId}/library?page={page}&size={size}`
- **특징:** 사용자가 결제를 완료하여 라이브러리에 소유 중인 게임 목록을 반환합니다.
- **Response JSON:** (게임 목록 전체 조회와 동일한 규격)

### 3.4 게임 파일 다운로드
- **Endpoint:** `GET /api/users/{userId}/library/{gameId}/download`
- **특징:** 라이브러리에 게임을 소유한 경우, 게임 파일(대체 텍스트)을 다운로드 형식으로 반환합니다.
- **Response Headers:**
  - `Content-Type: text/plain`
  - `Content-Disposition: attachment; filename="game1.txt"`
- **Response Body (텍스트 형식):**
```text
Game ID: game1

이 파일은 GDS 시스템에서 정상적으로 결제 후 다운로드된 게임 파일(대체 텍스트)입니다!
재밌게 즐겨주세요!
```
