### 1. 수정, 삭제 API의 request를 어떤 방식으로 사용하셨나요? (param, query, body)
 - 수정과 삭제 모두 path parameter와 body request를 사용하였습니다.
### 2. 어떤 상황에 어떤 방식의 request를 써야하나요?
 - 모든 요청에서 path parameter를 사용할 수 있고, GET 요청은 query param, POST, PUT, PATCH는 body, delete는 body를 지양하며 path 혹은 query parameter를 사용한다.
### 3. RESTful한 API를 설계했나요? 어떤 부분이 그런가요? 어떤 부분이 그렇지 않나요?
 - 처음에는 createBoard와 같은 URL을 사용하였는데, 이후 RESTful API를 확인하고 수정하였습니다.
 - delete API에서 body parameter를 지양해야 한다는 사실을 확인하였고, 이후 header로 password를 받는 방법을 고려하고 있습니다. 
### 4. 적절한 관심사 분리를 적용하였나요? (Controller, Repository, Service)
 - 파라미터 validation 및 사용자 인터페이스는 Controller
 - 비즈니스 로직은 service
 - domain entity와 데이터 처리는 repository
### 5. API 명세서 작성 가이드라인을 검색하여 직접 작성한 API 명세서와 비교해보세요!
 - 넵!