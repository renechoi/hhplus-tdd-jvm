package io.hhplus.tdd.cucumber.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import lombok.RequiredArgsConstructor;

/**
 * 데이터베이스 정리 작업을 수행하는 Executor 클래스.
 * <p>
 * 이 클래스는 메모리 기반 데이터베이스에서 특정 ID의 데이터를 초기화하는 역할을 합니다.
 * </p>
 *
 * @author : Rene Choi
 * @since : 2024/06/17
 */
@Component
@RequiredArgsConstructor
public class DatabaseCleanupExecutor implements InitializingBean {

	private final UserPointTable userPointTable;


	@Override
	public void afterPropertiesSet() throws Exception {

	}

	/**
	 * 데이터베이스 초기화 로직을 수행합니다.
	 * <p>
	 * 모든 사용자 ID를 조회하고 해당 ID의 포인트를 0으로 초기화합니다.
	 * </p>
	 */
	public void execute() {
		List<Long> allIds = getAllUserIds();
		for (Long id : allIds) {
			UserPoint userPoint = userPointTable.selectById(id);
			if (userPoint != null) {
				userPointTable.insertOrUpdate(id, 0);
			}
		}
	}

	/**
	 * 모든 사용자 ID를 반환합니다.
	 * <p>
	 * 과제 제약 사항에 따라 데이터베이스 내부 수정이 불가하므로, ID를 임의로 제공하여야 데이터를 초기화할 수 있는 상황에서,
	 * 사용되는 ID 기반의 리스트를 임의로 정의하여 반환합니다.
	 * </p>
	 *
	 * @return 모든 사용자 ID 리스트
	 */
	private List<Long> getAllUserIds() {
		return LongStream.rangeClosed(1, 10).boxed().collect(Collectors.toList());
	}
}
