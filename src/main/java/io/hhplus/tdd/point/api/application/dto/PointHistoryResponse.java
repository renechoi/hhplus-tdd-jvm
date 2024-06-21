package io.hhplus.tdd.point.api.application.dto;

import io.hhplus.tdd.point.api.domain.model.outport.PointHistoryInfo;
import io.hhplus.tdd.point.common.mapper.ObjectMapperBasedVoMapper;
import io.hhplus.tdd.point.common.model.types.TransactionType;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
public record PointHistoryResponse(
	long id,
	long userId,
	long amount,
	TransactionType type,
	long updateMillis
) {
	public static PointHistoryResponse from(PointHistoryInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, PointHistoryResponse.class);
	}
}
