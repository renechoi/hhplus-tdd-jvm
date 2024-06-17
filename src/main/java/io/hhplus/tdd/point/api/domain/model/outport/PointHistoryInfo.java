package io.hhplus.tdd.point.api.domain.model.outport;

import io.hhplus.tdd.point.api.domain.entity.PointHistory;
import io.hhplus.tdd.point.common.mapper.ObjectMapperBasedVoMapper;
import io.hhplus.tdd.point.common.model.types.TransactionType;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
public record PointHistoryInfo(
	long id,
	long userId,
	long amount,
	TransactionType type,
	long updateMillis
) {
	public static PointHistoryInfo from(PointHistory pointHistory) {
		return ObjectMapperBasedVoMapper.convert(pointHistory, PointHistoryInfo.class);
	}
}
