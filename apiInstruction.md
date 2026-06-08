# 게임 관련
1. 게임 목록 전체 보기
## GET /api/games
## 특징: 누구나 조회 가능 (세션 인증 생략)
## Json
{
  "status": "success",
  "message": "게임 목록 조회 성공",
  "totalCount": 2,
  "data": [
    {
      "id": "game1",
      "title": "Game One",
      "developer": "Sample Dev Studio"
      "genre": [
        "RPG",
        "horror"
      ],
      "price" : 15000,
      "demo" : "available"
      "distributionStatus" : "released"  
    },
    {
      "id": "game2",
      "title": "Game Two",
      "developer": "Sample Dev Studio"
      "genre": [
        "racing",
        "action"
      ],
      "price" : 22000,
      "demo" : "available"
      "distributionStatus" : "released"
    }
  ]
}
2. 게임 검색
## GET(사용자 입력어)/Response(입력어와 제목이 일치하는 부분이 있는 게임들)
## 특징: 이미 구매한 게임이어도 조회 가능
## 반환값은 '게임목록 전체보기'와 같음

3. 게임 상세 정보
## GET(세션아이디, 토큰, 게임아이디)/Response(게임의 상세 정보)
## 특징: 구매한 게임인지 아닌지에 따라서 응답에 다운로드 링크가 채워져 있거나 채워져있지 않음. 
## json

{
  "id": "game1",
  "developer": "Sample Dev Studio",
  "genre": [
    "RPG",
    "horror"
  ],
  "price": 15000,
  "isDemoAvailable": true,
  "description": "어두운 숲 속에서 살아남아야 하는 긴장감 넘치는 RPG 호러 게임입니다.",
  "fileSizeGb": 45.5,
  "distributionStatus": "released"
}
4. 장바구니 결제(구매) 확정 요청 (토스페이먼츠 연동)
## POST /api/purchase
## Request Body (userId, paymentKey, orderId, amount)

## 특징: 프론트엔드가 토스 위젯에서 결제를 마친 후 받은 결제 키를 백엔드로 보냅니다.
## 백엔드는 도메인 객체를 통해 총액 위변조를 검증하고, 토스 서버와 통신하여 최종 승인을 받습니다.
## 승인 성공 시 게임은 라이브러리에 추가되고 장바구니는 비워집니다.

## Request Json (임시 인증: userId 직접 전송)
{
  "userId": "user1",
  "paymentKey": "toss_abc123",
  "orderId": "order_20260603_1122",
  "amount": 37000
}

## Response Json (성공 시)
{
  "status": "success",
  "message": "결제 검증 및 구매 처리가 완료되었습니다.",
  "data": {
    "transactionId": "toss_abc123",
    "orderId": "order_20260603_1122",
    "totalPaidAmount": 37000
  }
}




## 5. 장바구니 담기 요청

## POST /api/cart
## Request(userId, gameId) / Response(장바구니 담기 성공 여부 및 담긴 게임 정보)

## 특징: 이미 구매한 게임은 장바구니에 담을 수 없음. 이미 장바구니에 있는 게임도 중복으로 담을 수 없음.

## Request Json

```json
{
  "userId": "user1",
  "gameId": "game1"
}
```

## Response Json

```json
{
  "status": "success",
  "message": "장바구니에 게임이 추가되었습니다.",
  "data": {
    "id": "game1",
    "title": "Game One",
    "price": 15000
  }
}
```

## 실패 Json - 이미 장바구니에 있는 경우

```json
{
  "status": "fail",
  "message": "이미 장바구니에 담긴 게임입니다.",
  "data": null
}
```

## 실패 Json - 이미 구매한 게임인 경우

```json
{
  "status": "fail",
  "message": "이미 라이브러리에 있는 게임입니다.",
  "data": null
}
```



## 6. 장바구니 삭제 요청

## DELETE(세션아이디, 토큰, 게임아이디) / Response(장바구니 삭제 성공 여부)

## 특징: 장바구니에 존재하는 게임만 삭제 가능. 삭제 후 프론트에서는 장바구니 목록을 다시 조회하거나 해당 항목을 화면에서 제거함.

## Json

```json
{
  "status": "success",
  "message": "장바구니에서 게임이 삭제되었습니다.",
  "data": {
    "id": "game1"
  }
}
```

## 실패 Json

```json
{
  "status": "fail",
  "message": "장바구니에 존재하지 않는 게임입니다.",
  "data": null
}
```

## 7. 구매한 게임 파일 다운로드 요청

## GET /api/users/{userId}/library/{gameId}/download
## 특징: 라이브러리에 있는 게임만 다운로드 가능. 구매하지 않은 게임의 다운로드를 요청하면 실패 응답을 반환함. 현재 실제 게임 파일이 없으므로 txt 파일 다운로드로 대체함.

## Response
```
json
{
  Content-Type: text/plain,
  Content-Disposition: attachment,
  filename="game1.txt"
 }
```


## 파일 내용 예시

```game1.txt
Game ID: game1
Title: Game One
Developer: Sample Dev Studio
Genre: RPG, horror
Description: RPG 호러 게임입니다.
```

## 실패 Json

```json
{
  "status": "fail",
  "message": "구매하지 않은 게임은 다운로드할 수 없습니다.",
  "data": null
}
```



# 유저 관련

## 8. 각 유저별 구매한 게임 목록 (라이브러리)

## GET /api/users/{userId}/library?page={page}&size={size}

## 특징: 게임 목록 전체 보기와 유사한 형태로 구매한 게임 목록을 반환함. 단, 라이브러리는 이미 구매한 게임만 보여주며 각 게임마다 다운로드 가능 여부와 다운로드 URL을 포함함.

## 특징: 라이브러리에서 상세보기를 누르면 상점 상세 페이지가 아니라 라이브러리 상세 페이지로 이동함. 라이브러리 상세 페이지에서는 파일 다운로드가 가능함.

## 특징: 구매한 게임 목록은 page와 size를 통해 100개 단위로 조회 가능함.

## Json

```json
{
  "status": "success",
  "message": "라이브러리 조회 성공",
  "totalCount": 2,
  "page": 1,
  "size": 100,
  "data": [
    {
      "id": "game1",
      "title": "Game One",
      "developer": "Sample Dev Studio",
      "genre": [
        "RPG",
        "horror"
      ],
      "price": 15000,
      "demo": "available",
      "distributionStatus": "released",
      "isDownloadable": true,
      "downloadUrl": "/api/users/user1/library/game1/download"
    },
    {
      "id": "game2",
      "title": "Game Two",
      "developer": "Sample Dev Studio",
      "genre": [
        "racing",
        "action"
      ],
      "price": 22000,
      "demo": "available",
      "distributionStatus": "released",
      "isDownloadable": true,
      "downloadUrl": "/api/users/user1/library/game2/download"
    }
  ]
}
```

## Json - 구매한 게임이 없는 경우

```json
{
  "status": "success",
  "message": "구매한 게임이 없습니다.",
  "totalCount": 0,
  "page": 1,
  "size": 100,
  "data": []
}
```

---

## 6. 라이브러리 게임 다운로드 요청 (일단 적어봄)

## GET(세션아이디, 토큰, 유저아이디, 게임아이디) / Response(구매한 게임 파일)

## 특징: 해당 유저의 라이브러리에 존재하는 게임만 다운로드할 수 있음. 구매하지 않은 게임의 다운로드를 요청하면 실패 응답을 반환함.

## 특징: 현재 실제 게임 실행 파일이 없으므로 txt 파일 다운로드로 대체 가능함.

## Response

```text
Content-Type: text/plain
Content-Disposition: attachment; filename="game1.txt"
```

## 파일 내용 예시

```game1.txt
Game ID: game1
Title: Game One
Developer: Sample Dev Studio
Genre: RPG, horror
Description: RPG 호러 게임입니다.
```
## 3. 로그인

## POST(아이디, 비밀번호) / Response(로그인 성공 여부, 세션아이디, 토큰, 유저 정보)

## 특징: 로그인할 때 게이머, 게임 개발자, 관리자를 직접 선택하지 않음. 서버가 계정 정보를 조회하여 관리자 계정인지, 개발자 계정인지, 게이머 계정인지 자동으로 판별함.

## 특징: 로그인 성공 후 게임 목록 화면으로 이동함. 단, 개발자와 관리자는 현재 전용 기능이 구현되지 않았으므로 안내 메시지만 출력함.

## Request Json

```json
{
  "userId": "user1",
  "password": "1234"
}
```

## Response Json - 회원 게이머

```json
{
  "status": "success",
  "message": "로그인 성공",
  "data": {
    "sessionId": "session_20260603_001",
    "accessToken": "token_abc123",
    "user": {
      "id": "user1",
      "name": "홍길동",
      "userType": "MemberGamer"
    }
  }
}
```

## Response Json - 게임 개발자

```json
{
  "status": "success",
  "message": "개발자 계정으로 로그인되었습니다.",
  "data": {
    "sessionId": "session_20260603_002",
    "accessToken": "token_dev_abc123",
    "user": {
      "id": "dev1",
      "name": "개발자A",
      "userType": "Developer"
    }
  }
}
```

## Response Json - 관리자

```json
{
  "status": "success",
  "message": "관리자 계정으로 로그인되었습니다.",
  "data": {
    "sessionId": "session_20260603_003",
    "accessToken": "token_admin_abc123",
    "user": {
      "id": "admin1",
      "name": "관리자A",
      "userType": "Administrator"
    }
  }
}
```

## 실패 Json

```json
{
  "status": "fail",
  "message": "아이디 또는 비밀번호가 일치하지 않습니다.",
  "data": null
}
```

---

## 4. 회원가입

## POST(아이디, 비밀번호, 이름) / Response(회원가입 성공 여부 및 가입된 유저 정보)

## 특징: 일반 회원가입은 회원 게이머 가입으로 처리함. 게임 개발자와 관리자는 별도의 내부 절차로 등록된 계정만 사용한다고 가정함.

## Request Json

```json
{
  "userId": "user1",
  "password": "1234",
  "name": "홍길동"
}
```

## Response Json

```json
{
  "status": "success",
  "message": "회원가입 성공",
  "data": {
    "id": "user1",
    "name": "홍길동",
    "userType": "MemberGamer"
  }
}
```

## 실패 Json

```json
{
  "status": "fail",
  "message": "이미 사용 중인 아이디입니다.",
  "data": null
}
```

---

## 5. 유저 정보 조회
