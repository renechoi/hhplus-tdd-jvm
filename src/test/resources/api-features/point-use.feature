Feature: 포인트 사용 기능
  Scenario: id와 amount가 주어질 때 point를 사용한다 - 기본 시나리오
    Given 사용자의 id가 1이고 충전 금액이 1000인 경우 포인트를 충전 요청하고 정상 응답을 받는다
    When 사용자의 id가 1이고 사용 금액이 500인 경우 포인트를 사용 요청하고 정상 응답을 받는다
    Then 사용자의 남은 포인트는 500이어야 한다

  Scenario: 유효하지 않은 id가 주어질 때 예외가 발생한다
    When 사용자의 id가 -1이고 사용 금액이 500인 경우 포인트를 사용 요청하면 예외가 발생한다

  Scenario: 음수 amount가 주어질 때 예외가 발생한다
    When 사용자의 id가 2이고 사용 금액이 -100인 경우 포인트를 사용 요청하면 예외가 발생한다

  Scenario: 충분한 포인트가 없을 때 예외가 발생한다
    Given 사용자의 id가 3이고 충전 금액이 300인 경우 포인트를 충전 요청하고 정상 응답을 받는다
    When 사용자의 id가 3이고 사용 금액이 500인 경우 포인트를 사용 요청하면 예외가 발생한다

  Scenario: 동시성 테스트 - 동시에 사용 요청을 보내더라도 보낸 값들이 전부 제대로 반영이 되어야 한다
    Given 사용자의 id가 4이고 충전 금액이 50000인 경우 포인트를 충전 요청하고 정상 응답을 받는다
    When 사용자의 id가 4이고 동시에 사용 금액이 500인 요청을 10번 보낸다
    Then 사용자의 남은 포인트는 45000이어야 한다
