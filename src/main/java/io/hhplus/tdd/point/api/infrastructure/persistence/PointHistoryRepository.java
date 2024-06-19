package io.hhplus.tdd.point.api.infrastructure.persistence;

import java.util.List;

import io.hhplus.tdd.point.api.domain.entity.PointHistory;
import io.hhplus.tdd.point.common.model.types.TransactionType;

/**
 * @author : Rene Choi
 * @since : 2024/06/19
 */
public interface PointHistoryRepository {
	PointHistory insert(long userId, long amount, TransactionType type, long updateMillis);
	List<PointHistory> selectAllByUserId(long userId);
}