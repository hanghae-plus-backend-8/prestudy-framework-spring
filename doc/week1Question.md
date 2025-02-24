### LocalDateTime 테스트
 - 시간에 관련한 로직을 테스트하는 경우 파라미터로 주입 받는 것이 testable한 코드라고 일전 강의에서 본 적이 있다.
 - 하지만 거의 모든 Entity에는 created_datetime, last_modified_datetime을 포함하고 있는 경우가 많고, 이를 모두 테스트 하기에는 오버 테스트라고 개인적으로 생각이 든다.
 - 제가 본 강의에서도 '주문' domain에서 특정 시간대에만 주문을 할 수 있는 요구조건이 있기 때문에 해당 datetime을 외부 파라미터로 받아서 테스트를 진행하였음.
 - 위와 같은 시간과 관련한 로직이 있지 않는 한 domain 혹은 repository에서만 파라미터로 주입받고, service에서 localdatetime.now()를 사용해도 되지 않을지 다른 사람들의 의견이 궁금

### 