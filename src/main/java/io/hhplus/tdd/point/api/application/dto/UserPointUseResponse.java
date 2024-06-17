package io.hhplus.tdd.point.api.application.dto;

import io.hhplus.tdd.point.api.domain.model.outport.UserPointChargeInfo;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointUseInfo;
import io.hhplus.tdd.point.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public record UserPointUseResponse(
	long id,
	long point,
	long updateMillis
) {

	public static UserPointUseResponse from(UserPointUseInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, UserPointUseResponse.class);
	}
}
