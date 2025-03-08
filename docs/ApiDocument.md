# API 명세서

## 게시글 API

### 게시글 조회

**[Description]**  
게시글 목록을 조회한다.

**[Request]**

+ URL : `/api/v1/boards`
+ Method : `GET`

**[Response]**

+ Response

```json
{
  "code": 200,
  "message": "OK",
  "data": [
    {
      "id": 9007199254740991,
      "title": "string",
      "content": "string",
      "writer": "string",
      "createdDate": "2025-02-15T00:02:42.261Z"
    }
  ]
}
```

+ Response Fields

| Path              | Type   | Description |
|-------------------|--------|-------------|
| code              | Number | 응답 코드       |
| message           | String | 응답 메세지      |
| data[].id         | Number | 게시글 ID      |
| data[].title      | String | 게시글 제목      |
| data[].content    | String | 게시글 내용      |
| data[].writer     | String | 게시글 작성자     |
| data[].createDate | String | 게시글 생성일시    |

### 게시글 상세 조회

**[Description]**  
선택한 게시글을 조회한다.

**[Request]**

+ URL : `/api/v1/boards/{id}`
+ Method : `GET`
+ Path Parameters

| Parameter | Description |
|-----------|-------------|
| id        | 게시글 ID      |

**[Response]**

+ Response

```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "id": 9007199254740991,
    "title": "string",
    "content": "string",
    "writer": "string",
    "createdDate": "2025-02-15T00:02:42.261Z"
  }
}
```

+ Response Fields

| Path            | Type   | Description |
|-----------------|--------|-------------|
| code            | Number | 응답 코드       |
| message         | String | 응답 메세지      |
| data.id         | Number | 게시글 ID      |
| data.title      | String | 게시글 제목      |
| data.content    | String | 게시글 내용      |
| data.writer     | String | 게시글 작성자     |
| data.createDate | String | 게시글 생성일시    |

### 게시글 생성

**[Description]**  
게시글을 작성한다.

**[Request]**

+ URL : `/api/v1/boards`
+ Method : `POST`
+ Request Body 
```json
{
  "title": "string",
  "content": "string",
  "writer": "string",
  "password": "string"
}
```
+ Request Fields

| Path     | Type   | Required | Description |
|----------|--------|----------|-------------|
| title    | String | true     | 게시글 제목      |
| content  | String | true     | 게시글 내용      |
| writer   | String | true     | 게시글 작성자명    |
| password | String | true     | 게시글 패스워드    |


**[Response]**

+ Response

```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "id": 9007199254740991,
    "title": "string",
    "content": "string",
    "writer": "string",
    "createdDate": "2025-02-15T00:02:42.261Z"
  }
}
```

+ Response Fields

| Path            | Type   | Description |
|-----------------|--------|-------------|
| code            | Number | 응답 코드       |
| message         | String | 응답 메세지      |
| data.id         | Number | 게시글 ID      |
| data.title      | String | 게시글 제목      |
| data.content    | String | 게시글 내용      |
| data.writer     | String | 게시글 작성자     |
| data.createDate | String | 게시글 생성일시    |

### 게시글 수정

**[Description]**  
게시글을 수정한다.

**[Request]**

+ URL : `/api/v1/boards/{id}`
+ Method : `PUT`
+ Path Parameters

| Parameter | Description |
|-----------|-------------|
| id        | 게시글 ID      |

+ Request Body
```json
{
  "title": "string",
  "content": "string",
  "writer": "string",
  "password": "string"
}
```
+ Request Fields

| Path     | Type   | Required | Description |
|----------|--------|----------|-------------|
| title    | String |          | 게시글 제목      |
| content  | String |          | 게시글 내용      |
| writer   | String |          | 게시글 작성자명    |
| password | String |          | 게시글 패스워드    |


**[Response]**

+ Response

```json
{
  "code": 200,
  "message": "OK",
  "data": {
    "id": 9007199254740991,
    "title": "string",
    "content": "string",
    "writer": "string",
    "createdDate": "2025-02-15T00:02:42.261Z"
  }
}
```

+ Response Fields

| Path            | Type   | Description |
|-----------------|--------|-------------|
| code            | Number | 응답 코드       |
| message         | String | 응답 메세지      |
| data.id         | Number | 게시글 ID      |
| data.title      | String | 게시글 제목      |
| data.content    | String | 게시글 내용      |
| data.writer     | String | 게시글 작성자     |
| data.createDate | String | 게시글 생성일시    |

### 게시글 삭제

**[Description]**  
게시글을 삭제한다.

**[Request]**

+ URL : `/api/v1/boards/{id}`
+ Method : `DELETE`
+ Path Parameters

| Parameter | Description |
|-----------|-------------|
| id        | 게시글 ID      |

+ Request Body
```json
{
  "password": "string"
}
```
+ Request Fields

| Path     | Type   | Required | Description |
|----------|--------|----------|-------------|
| password | String | true     | 게시글 패스워드    |


**[Response]**

+ Response

```json
{
  "code": 200,
  "message": "OK"
}
```

+ Response Fields

| Path            | Type   | Description |
|-----------------|--------|-------------|
| code            | Number | 응답 코드       |
| message         | String | 응답 메세지      |

## 사용자 API

### 회원가입

**[Description]**  
사용자가 회원가입을 한다.

**[Request]**

+ URL : `/api/v1/users`
+ Method : `POST`
+ Request Body
```json
{
  "username": "string",
  "password": "string"
}
```
+ Request Fields

| Path     | Type   | Required | Description |
|----------|--------|----------|-------------|
| username | String | true     | 사용자명        |
| password | String | true     | 사용자 패스워드    |

**[Response]**

+ Response

```json
{
  "code": 200,
  "message": "OK"
}
```

### 로그인

**[Description]**  
사용자가 회원가입한 정보로 로그인을 한다.

**[Request]**

+ URL : `/api/v1/users/login`
+ Method : `POST`
+ Request Body
```json
{
  "username": "string",
  "password": "string"
}
```
+ Request Fields

| Path     | Type   | Required | Description |
|----------|--------|----------|-------------|
| username | String | true     | 사용자명        |
| password | String | true     | 사용자 패스워드    |

**[Response]**

+ Header : `Authorization : Bearer {JWT Token}`
+ Response

```json
{
  "code": 200,
  "message": "OK"
}

