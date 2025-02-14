1. 프로젝트 요약 (Summary)  
   실 유저가 사용할 수 있는 회원(회원가입 및 로그인) 및 게시판(CRUD) 기능을 API로 구현합니다.

2. 목표 (Goals)
	+ 게시글을 등록/수정/조회 기능을 제공합니다.
	+ 회원가입과 로그인 기능을 제공합니다.
	+ 댓글 작성/수정/삭제 기능을 제공합니다.

3. 계획 (Plan)
	+ 방법론 적용 : TDD, 클린 아키텍처
	+ 기술 스택 : Spring Boot, JPA, AWS, Github Actions
	+ 문서화 : UseCase / ERD / API 명세서 / API Swagger
	+ 멀티 모듈을 고려한 패키지 구조 설계 - 레이어 계층 (MVC 패턴)  
	  코어 모듈에서 사용하는 엔티티와 Repository Layer를 같은 domain 패키지에 구현하며, API는 따로 패키지를 분리한다.  
	  API 패키지 내에서는 Controller Layer와 Service Layer로 나뉘며 Request DTO또한 분리한다.   
	  Controller Layer에서의 요청 DTO를 *Request, Service Layer에서는 *Command를 사용한다.
	+ 공통 API 응답
	  ```json
	  {
		  "code" : 200,
		  "message" : "",
		  "data" : {}
	  } 
	  ```
	+ 공통 예외처리  
	  @RestControllerAdivce를 통해 공통으로 예외처리를 관리한다.
	+ 이슈 / PR 과제 제출  
	  이슈 : PR에 대한 기능 구현의 이슈를 등록한다. 질문 및 논의 성격의 이슈도 같이 등록하고 스터디 시간에 논의   
	  PR : 생성한 이슈에 대한 PR을 매주 스터디 기간 전에 요청한다. 작성한 PR을 리뷰 받는다.

4. 마일스톤 (Milestones)
	1. 0주차 (2.14 ~ 2.17) : Tech Spec 작성
	2. 1주차 (2.18 ~ 2.24) : Use Case 작성 / ERD 설계 / API 명세서 작성 / 게시글 기능 구현
	3. 2주차 (2.25 ~ 3.3) : AWS 배포 / 도메인 연결 / 회원가입 및 로그인 기능 구현
	4. 3주차 (3.4 ~ 3.10) : 회원 권한 부여 / 댓글 기능 구현 / 예외처리
	5. 4주차 (3.11 ~ 3.17) : 게시글 API 업그레이드 / 회고 
      