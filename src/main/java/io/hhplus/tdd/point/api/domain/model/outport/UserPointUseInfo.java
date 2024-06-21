package io.hhplus.tdd.point.api.domain.model.outport;

import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import io.hhplus.tdd.point.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public record UserPointUseInfo(
	long id,
	long point,
	long updateMillis
) {
	public static UserPointUseInfo from(UserPoint updatedUserPoint) {
		return ObjectMapperBasedVoMapper.convert(updatedUserPoint, UserPointUseInfo.class);
	}
}
