package io.hhplus.tdd.point.common.transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserPointTableProxy {

	private final TransactionContext transactionContext;
	private final UserPointTable userPointTable;

	private final Set<Long> userIds = ConcurrentHashMap.newKeySet(); // 모든 사용자 ID를 추적하기 위한 Set

	public UserPoint selectById(Long id) {
		userIds.add(id); // 사용자 조회 시 ID를 추적
		return userPointTable.selectById(id);
	}

	public UserPoint insertOrUpdate(long id, long amount) {
		userIds.add(id); // 사용자 업데이트 시 ID를 추적
		return userPointTable.insertOrUpdate(id, amount);
	}

	public void beginTransaction() {
		Map<Long, UserPoint> currentState = new HashMap<>();
		for (Long id : userIds) {
			UserPoint userPoint = userPointTable.selectById(id);
			currentState.put(id, UserPointUtil.copy(userPoint));
		}
		transactionContext.begin(currentState);
	}

	public void rollbackTransaction() {
		Map<Long, UserPoint> currentState = new HashMap<>();
		for (Long id : userIds) {
			UserPoint userPoint = userPointTable.selectById(id);
			currentState.put(id, UserPointUtil.copy(userPoint));
		}
		transactionContext.rollback(currentState);
		for (Map.Entry<Long, UserPoint> entry : currentState.entrySet()) {
			userPointTable.insertOrUpdate(entry.getKey(), entry.getValue().point());
		}
	}

	public void commitTransaction() {
		transactionContext.commit();
	}
}

