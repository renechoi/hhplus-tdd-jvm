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



## 어떻게 해결할까?

본 프로젝트에서는 분산환경을 고려하지 않으며 Database를 메모리로 사용하되 해당 클래스에 대한 수정을 가하지 않는 제약 사항을 갖고 있다.

이 점을 고려할 때  [자바에서 동시성 문제를 다루는 n가지 방법들](https://renechoi.notion.site/2024-03-n-feat-6553bbe3b4a049b8a6b0661ac4156bf9) 에서 다룬 방식으로

다음과 같이 접근 방법을 탐색해볼 수 있다.

- 락 기반 동기화: 간단하고 직관적이지만, 락 경쟁이나 데드락 등의 문제가 발생할 수 있음. <br>
- 락 프리 접근 방식(CAS, Atomic 클래스 사용): 락을 사용하지 않고 동시성 문제를 해결하며, 성능이 더 좋음.<br>
- 식별자 메커니즘: 고유 식별자를 사용하여 요청의 중복을 방지하는 간단한 방법. <br>
- 메시지 큐 모방 태스크 관리: 큐를 이용해 요청을 순차적으로 처리하여 동시성 문제를 우아하게 해결. <br>

현재 어플리케이션을 예시로 하나씩 살펴보자.


### 고전적 락 기반 동기화
프로그래밍 언어에서 제공하는 명령어 수준의 락을 사용하여 특정 코드 블록의 실행을 한 스레드만 수행하도록 제한한다.

**synchronized 사용 예시**
```java
public synchronized UserPointChargeInfo charge(UserPointChargeCommand chargeCommand) {
    // 포인트 조회, 계산 및 업데이트 로직
}
```

**ReentrantLock 사용 예시**
```java
private final ReentrantLock lock = new ReentrantLock();

public UserPointChargeInfo charge(UserPointChargeCommand chargeCommand) {
    lock.lock();
    try {
        // 포인트 조회, 계산 및 업데이트 로직
    } finally {
        lock.unlock();
    }
}
```

### 락 프리 접근: CAS 알고리즘

`java.util.concurrent` 를 활용하여 동시성을 해결한다. 마찬가지로 컴퓨터 명령어 수준에서 제공되는 동시성 제어 방법인 CAS(Compare-And-Swap) 연산을 활용하는 것이다. 내부 로직 자체는 엄밀히 말하면 낙관적 락과 유사하다.

**AtomicLong 사용 예시**
```java
public class UserPoint {
    private final long id;
    private final AtomicLong point;
    private final long updateMillis;

    public UserPoint deductPoints(long amount) {
        long current;
        long updated;
        do {
            current = this.point.get();
            if (amount > current) {
                throw new IllegalArgumentException("잔액이 충분하지 않습니다.");
            }
            updated = current - amount;
        } while (!this.point.compareAndSet(current, updated));
        return new UserPoint(this.id, updated, System.currentTimeMillis());
    }
}
```

### 식별자 메커니즘
각 요청에 고유 식별자를 부여하고, 이를 통해 중복 처리를 방지한다.

**식별자 기반 접근 예시**
```java
public UserPointChargeInfo charge(UserPointChargeCommand chargeCommand) {
    String identifier = chargeCommand.id() + ":" + chargeCommand.amount();
    if (!IdentifierLockManager.tryLock(identifier)) {
        throw new IllegalStateException("이미 처리 중인 요청입니다.");
    }
    try {
        // 포인트 조회, 계산 및 업데이트 로직
    } finally {
        IdentifierLockManager.unlock(identifier);
    }
}
```

### 메시지 큐를 모방한 태스크 관리
메시지 큐를 사용하여 요청을 순차적으로 처리한다.

**메시지 큐 모방 예시:**
```java
private final ConcurrentLinkedQueue<UserPointChargeCommand> queue = new ConcurrentLinkedQueue<>();

public void chargePoints() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    executorService.submit(() -> {
        while (true) {
            UserPointChargeCommand command = queue.poll();
            if (command != null) {
                chargePoint(command);
            }
        }
    });
}

public void chargePoint(UserPointChargeCommand chargeCommand) {
    queue.offer(chargeCommand);
}

private void chargePoint(UserPointChargeCommand chargeCommand) {
    // 포인트 조회, 계산 및 업데이트 로직
}
```




## 실제 해결한 방식

본 프로젝트에서는 어플리케이션 수준에서 동시성 제어를 위해 Redis 분산락을 시뮬레이션한 어노테이션 기반 락 관리 방식을 채택했다. Spring AOP와 ConcurrentHashMap, ReentrantLock을 활용하여 구현했다.

### 분산락 어노테이션 및 AOP

어노테이션 `@DistributedLock`을 사용하여 메서드에 락을 적용한다. 이를 통해 특정 메서드가 호출될 때 자동으로 락을 획득하고 해제한다.

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    String prefix() default "";
    String[] keys() default {};
}
```

### DistributedLockManager

`DistributedLockManager` 클래스는 Redis의 싱글 스레드 동작 방식을 시뮬레이션하여 어플리케이션 레벨에서 동시성 제어를 수행하는 클래스이다. `ConcurrentHashMap`과 `ReentrantLock`을 사용하여 락을 관리하며, 특정 키에 대한 락을 획득하고 해제하는 기능을 제공한다.


```java
/**
 * 분산 락 매니저 (DistributedLockManager) 클래스는 어플리케이션 레벨에서 동시성 제어를 위한 주요 기능을 제공하며,
 * 특히 멀티스레드 환경에서 임계 영역(Critical Section)을 보호하는 역할을 담당합니다.
 * 이 클래스는 데이터베이스 차원의 동시성 제어 대신, 어플리케이션 차원에서 락을 관리하여 높은 성능과 유연성을 제공합니다.
 *
 * <p>주요 기능 및 역할:</p>
 *
 * <ul>
 *   <li><b>임계 영역 보호:</b> 다중 스레드가 동시에 접근할 수 있는 임계 영역을 보호하여 데이터 무결성을 유지합니다.</li>
 *   <li><b>싱글스레드 시뮬레이션:</b> 특정 자원에 대해 한 번에 하나의 스레드만 접근할 수 있도록 보장합니다. 이는 Redis와 같은 분산 캐시 시스템의 싱글 스레드 처리 모델을 어플리케이션 레벨에서 시뮬레이션한 것입니다.</li>
 *   <li><b>원자적 연산:</b> ConcurrentHashMap과 ReentrantLock을 사용하여 락의 생성 및 해제 연산이 원자적으로 수행되도록 보장합니다. 이를 통해 락 관리의 일관성과 안정성을 제공합니다.</li>
 * </ul>
 *
 * <p>주요 메서드:</p>
 *
 * <ul>
 *   <li>{@link #lock(String)}: 주어진 키에 대해 락을 획득합니다. 만약 해당 키에 대한 락이 존재하지 않으면, 새로운 ReentrantLock을 생성하여 락을 획득합니다.</li>
 *   <li>{@link #unlock(String)}: 주어진 키에 대해 락을 해제합니다. 이미 존재하는 락을 해제하며, 락이 존재하지 않을 경우 아무런 작업도 수행하지 않습니다.</li>
 * </ul>
 */
@Component
public class DistributedLockManager {
    private final ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();

    public void lock(String key) {
        locks.computeIfAbsent(key, k -> new ReentrantLock()).lock();
    }

    public void unlock(String key) {
        Lock lock = locks.get(key);
        if (lock != null) {
            lock.unlock();
        }
    }
}
```

### AOP를 이용한 락 적용

AOP를 이용하여 메서드 호출 시 락을 적용한다. `@Around` 어노테이션을 사용하여 `@DistributedLock`이 붙은 메서드의 전후에 락을 획득하고 해제하는 로직을 삽입힌다.

```java
@Aspect
@Component
public class DistributedLockAspect {

    private final DistributedLockManager lockManager;

    @Autowired
    public DistributedLockAspect(DistributedLockManager lockManager) {
        this.lockManager = lockManager;
    }

    @Around("@annotation(lockAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock lockAnnotation) throws Throwable {
        String key = generateKey(joinPoint, lockAnnotation);
        lockManager.lock(key);
        try {
            return joinPoint.proceed();
        } finally {
            lockManager.unlock(key);
        }
    }

    private String generateKey(ProceedingJoinPoint joinPoint, DistributedLock lockAnnotation) {
        String prefix = lockAnnotation.prefix();
        String[] keys = lockAnnotation.keys();
        String keyString = Arrays.stream(keys)
            .map(key -> getValueFromArgs(joinPoint, key))
            .collect(Collectors.joining(":"));
        return prefix + ":" + keyString;
    }

    private String getValueFromArgs(ProceedingJoinPoint joinPoint, String key) {
        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        return parser.parseExpression("#" + key).getValue(context, String.class);
    }
}
```

### 서비스 코드에 락 적용

포인트 충전 및 사용 서비스 메서드에 `@DistributedLock` 어노테이션을 적용하여 동시성 문제를 해결한다.

```java
@Facade
@RequiredArgsConstructor
public class PointFacade {

    private final PointService pointService;
    private final UserPointChargeValidator validator;
    private final UserPointUseValidator useValidator;

    @DistributedLock(prefix = "charge", keys = {"chargeRequest.id"})
    public UserPointChargeResponse charge(UserPointChargeRequest chargeRequest) {
        validator.validate(chargeRequest);
        return UserPointChargeResponse.from(pointService.charge(chargeRequest.toCommand()));
    }

    @DistributedLock(prefix = "use", keys = {"userPointUserRequest.id"})
    public UserPointUseResponse use(UserPointUserRequest userPointUserRequest) {
        useValidator.validate(userPointUserRequest);
        return UserPointUseResponse.from(pointService.use(userPointUserRequest.toCommand()));
    }
}
```



## 결과 

결과를 살펴보자. 

충전과 사용 API에 대한 동시 요청에 대해 순차적으로 처리하여 원하는 결과가 나타난 것을 볼 수 있다. 

##### 동시 요청의 흔적

![concurrency_request.png](docs%2Fconcurrency_request.png)

![concurrency_request_2.png](docs%2Fconcurrency_request_2.png)


##### 동시 요청 처리 결과 

![concurrency_result1.png](docs%2Fconcurrency_result1.png)

![concurrency_result2.png](docs%2Fconcurrency_result2.png)




