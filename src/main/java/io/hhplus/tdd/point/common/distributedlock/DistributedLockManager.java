package io.hhplus.tdd.point.common.distributedlock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

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
 *
 * <p>사용 예:</p>
 * <pre>
 * {@code
 * DistributedLockManager lockManager = new DistributedLockManager();
 * String key = "resourceKey";
 *
 * lockManager.lock(key);
 * try {
 *     // 임계 영역 보호 코드
 * } finally {
 *     lockManager.unlock(key);
 * }
 * }
 * </pre>
 *
 * @see java.util.concurrent.locks.ReentrantLock
 * @see java.util.concurrent.ConcurrentHashMap
 * @see java.util.concurrent.locks.Lock
 * @since 2024/06/17
 * @author Rene Choi
 */
@Component
public class DistributedLockManager {
	private final ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();

	/**
	 * 주어진 키에 대해 락을 획득합니다.
	 * 키에 대한 락이 존재하지 않으면 새로운 ReentrantLock을 생성하여 락을 획득합니다.
	 *
	 * @param key 락을 획득할 키
	 */
	public void lock(String key) {
		locks.computeIfAbsent(key, k -> new ReentrantLock()).lock();
	}

	/**
	 * 주어진 키에 대해 락을 해제합니다.
	 * 키에 대한 락이 존재하지 않으면 아무 작업도 수행하지 않습니다.
	 *
	 * @param key 락을 해제할 키
	 */
	public void unlock(String key) {
		Lock lock = locks.get(key);
		if (lock != null) {
			lock.unlock();
		}
	}
}