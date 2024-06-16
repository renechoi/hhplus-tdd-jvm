package io.hhplus.tdd.point.api.domain.model.outport;

import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import io.hhplus.tdd.point.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public record UserPointChargeInfo(
	long id,
	long point,
	long updateMillis
) {
	public static UserPointChargeInfo from(UserPoint updatedUserPoint) {
		return ObjectMapperBasedVoMapper.convert(updatedUserPoint, UserPointChargeInfo.class);
	}
}
