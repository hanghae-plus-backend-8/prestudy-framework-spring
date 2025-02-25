# 1주차 질문

### 1️⃣ 수정, 삭제 API의 request를 어떤 방식으로 사용하셨나요? (param, query, body)

수정과 삭제에 필요한 선택한 게시글의 ID를 URI의 Pathvariable을 사용하였으며,   
비밀번호을 포함한 ID 외의 파라미터는 요청 바디로 구성하였습니다.  
이는 RESTful API를 최대한 준수하기 위해서 입니다. 

### 2️⃣ 어떤 상황에 어떤 방식의 request를 써야하나요?

RESTful API를 구성할 시에는 Http 메서드 별로 request 방식이 달라집니다.   
GET의 경우 조회 API로 사용되며 요청은 쿼리 파라미터로 구성합니다.  
POST는 주로 생성 API로 사용되며 요청은 바디로 구성합니다.   
PUT, PATCH는 주로 수정 API로 사용되며 각각 PUT은 전체 수정, PATCH는 일부 수정할 때 사용됩니다. 파라미터는 
식별자가 포함된 엔드 포인트로 구성합니다.   
DELETE는 주로 삭제 API로 사용되며 식별자를 포함한 Pathvariable을 사용합니다.

### 3️⃣ RESTful한 API를 설계했나요? 어떤 부분이 그런가요? 어떤 부분이 그렇지 않나요?

GET, POST, PUT 메서드를 이용하여 각각 조회, 생성, 수정 API를 RESTful API로 설계했습니다.  
DELETE의 경우 비밀번호를 받아야하는 요구사항이 있어 Request Body를 사용하여 RESTful API에 적합하지 않습니다.

### 4️⃣ 적절한 관심사 분리를 적용하였나요? (Controller, Repository, Service)

네, 레이어 계층을 적용하였고, 테스트 또한 레이어 별로 작성 하였습니다.  
Repository, Service 레이어는 각각의 단위테스트를 케이스 별 TDD로 구현하였습니다.
Controller 레이어는 Service 레이어에서 테스트를 하였기 때문에 Mocking 테스트를 진행하였습니다.  
또한, Controller 레이어에서 파라미터를 Service 레이어에 영향받지 않게 DTO를 분리하여 적절하게 관심사를 분리하였습니다. 

### 5️⃣ API 명세서 작성 가이드라인을 검색하여 직접 작성한 API 명세서와 비교해보세요!

