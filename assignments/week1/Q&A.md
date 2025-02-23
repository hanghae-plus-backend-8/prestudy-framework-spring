## 1주차 질문

> 수정, 삭제 API의 request를 어떤 방식으로 사용하셨나요? (param, query, body)

수정 API
  - path parameter: {id}
  - query parameter: X
  - body parameter: title, author, content, password

삭제 API
  - path parameter: {id}
  - query parameter: password
  - body parameter: X

> 어떤 상황에 어떤 방식의 request를 써야하나요?

- 모든 URI에 path paramter 사용 가능
- GET은 query parameter 사용
- POST, PUT, PATCH는 body parameter 사용
- DELETE는 body parameter 사용 지양
  - 호환성 문제: HTTP 클라이언트들중 DELETE 메서드는 body를 지원하지 않는 경우가 있음
  - 명세의 모호성: RFC 7231의 DELETE 메서드 설명에서는 요청 본문의 처리 방식에 대해 구체적으로 정의하지 않음


> RESTful한 API를 설계했나요? 어떤 부분이 그런가요? 어떤 부분이 그렇지 않나요?

- HTTP URI를 통해 자원을 명시
  - ex) /posts/{id}
- HTTP Method를 통해 행위를 명시
  - ex) /posts (POST)

> 적절한 관심사 분리를 적용하였나요? (Controller, Repository, Service)

- Controller: 사용자 인터페이스 처리
- Service: 비즈니스 로직 처리
- Repository: 데이터 처리

> API 명세서 작성 가이드라인을 검색하여 직접 작성한 API 명세서와 비교해보세요!