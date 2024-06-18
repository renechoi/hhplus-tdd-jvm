# 항해 플러스 [ 1주차 과제 ] TDD 로 개발하기

## 요구사항 

> point 패키지의 TODO 와 테스트코드를 작성해주세요.

- PATCH  /point/{id}/charge : 포인트를 충전한다.
- PATCH /point/{id}/use : 포인트를 사용한다.
- GET /point/{id} : 포인트를 조회한다.
- GET /point/{id}/histories : 포인트 내역을 조회한다.
- 잔고가 부족할 경우, 포인트 사용은 실패하여야 합니다.
- 동시에 여러 건의 포인트 충전, 이용 요청이 들어올 경우 순차적으로 처리되어야 합니다.


## 동시성 이슈는 왜 발생하는 것일까? 

분산 환경이 아님에도 멀티 스레딩 환경에서 동시성 이슈는 발생한다. 

서비스 클래스에서 포인트 충전 기능을 수행하는 함수를 살펴보자.  

```java
	public UserPointChargeInfo charge(UserPointChargeCommand chargeCommand) {
		UserPoint currentUserPoint = userPointTable.selectById(chargeCommand.id());

		long newPointAmount = currentUserPoint.calculateNewPointWithSummation(chargeCommand.amount());

		UserPoint updatedUserPoint = userPointTable.insertOrUpdate(chargeCommand.id(), newPointAmount);

		return UserPointChargeInfo.from(updatedUserPoint);
	}
}
```
`charge` 메서드는 다음과 같은 단계로 로직을 수행한다.

1. 주어진 id로 현재 유저 포인트를 조회한다. 
2. 새로운 포인트 금액을 계산한다.
3. 계산된 금액으로 유저 포인트를 업데이트한다. 

다음은 동시에 요청을 발생시키는 테스트 코드이다. 


> Scenario: 동시성 테스트 1 - 동시에 요청을 보내더라도 보낸 값들이 전부 제대로 반영이 되어야 한다<br>
Given 사용자의 id가 5이고 충전 금액이 1000인 경우 포인트를 충전 요청하고 정상 응답을 받는다<br>
When 사용자의 id가 5이고 동시에 충전 금액이 500인 요청을 10번 보낸다<br>
Then 사용자의 포인트는 6000이어야 한다<br>


```java
private void chargePointsConcurrently(Long id, Long amount, int times) {
		ExecutorService executorService = Executors.newFixedThreadPool(times);
		CountDownLatch latch = new CountDownLatch(times);

		IntStream.range(0, times).forEach(i -> {
			executorService.submit(() -> {
				awaitLatch(latch);
				chargePointRequestedWithSuccessResponse(id, amount);
			});
			latch.countDown();
		});

		executorService.shutdown();
		awaitTermination(executorService, 10, TimeUnit.SECONDS);
	}
```

동시성을 제어하지 않았을 때 이렇게 동시 요청을 보내면 어떻게 될까? 

사용자의 포인트는 6000이 기대되지만 실제로는 2500으로 확인되는 것을 볼 수 있다. 

![conccurrency.png](docs%2Fconccurrency.png)


왜 이러한 현상이 발생하는 것일까? 

다음과 같은 상황이다. 먼저 다이어그램을 살펴보자. 


![conccurency_sequence.png](docs%2Fconccurency_sequence.png)

흐름을 살펴보면 다음과 같다. 

1. **포인트 충전 요청**: `Thread A`와 `Thread B`가 거의 동시에 같은 유저 ID로 포인트 충전을 요청한다.
2. **포인트 조회**:
    - `Thread A`와 `Thread B`가 각각 `selectById`를 호출하여 초기 포인트 10을 반환받는다.
3. **포인트 계산**:
    - `Thread A`와 `Thread B` 모두 10 + 10 = 20으로 새로운 포인트 금액을 계산한다.
4. **포인트 업데이트**:
    - `Thread A`가 포인트를 20으로 업데이트하고, 이어서 `Thread B`도 포인트를 20으로 업데이트하여 `Thread A`의 변경 사항이 덮어씌워진다.
5. **응답 반환**:
    - 두 스레드 모두 최종 포인트 20으로 응답을 반환한다.

이 과정에서 `Thread A`와 `Thread B`의 업데이트가 충돌하여 데이터 불일치가 발생한다.

만약 정상적으로 진행되었다면, 즉 순차 처리가 되었다면 기댓값은 30이다. 
먼저 10 + 10 = 20으로 포인트를 업데이트 하고, 업데이트 된 20에 10을 더해 30이 나온다. 

이와 같이 멀티 스레딩 환경에서 동일 자원에 대해 분리된 연산을 수행하는 과정에서 빈번하게 발생한다. 


