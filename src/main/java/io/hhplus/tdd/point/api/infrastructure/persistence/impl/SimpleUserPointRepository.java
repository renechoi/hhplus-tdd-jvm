package io.hhplus.tdd.point.api.infrastructure.persistence.impl;

import org.springframework.stereotype.Repository;

import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import io.hhplus.tdd.point.api.infrastructure.database.UserPointTable;
import io.hhplus.tdd.point.api.infrastructure.persistence.UserPointRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/19
 */
@Repository
@RequiredArgsConstructor
public class SimpleUserPointRepository implements UserPointRepository {
	private final UserPointTable userPointTable;

	@Override
	public UserPoint selectById(Long id) {
		return userPointTable.selectById(id);
	}

	@Override
	public UserPoint insertOrUpdate(long id, long amount) {
		return userPointTable.insertOrUpdate(id, amount);
	}
}
