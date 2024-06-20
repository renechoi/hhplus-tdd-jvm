package io.hhplus.tdd.point.api.infrastructure.persistence.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import io.hhplus.tdd.point.api.domain.entity.PointHistory;
import io.hhplus.tdd.point.api.infrastructure.database.PointHistoryTable;
import io.hhplus.tdd.point.api.infrastructure.persistence.PointHistoryRepository;
import io.hhplus.tdd.point.common.model.types.TransactionType;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/19
 */
@Repository
@RequiredArgsConstructor
public class SimplePointHistoryRepository implements PointHistoryRepository {

	private final PointHistoryTable pointHistoryTable;

	@Override
	public PointHistory insert(long userId, long amount, TransactionType type, long updateMillis) {
		return pointHistoryTable.insert(userId, amount, type, updateMillis);
	}

	@Override
	public List<PointHistory> selectAllByUserId(long userId) {
		return pointHistoryTable.selectAllByUserId(userId);
	}
}
