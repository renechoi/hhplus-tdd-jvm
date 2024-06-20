package io.hhplus.tdd.point.api.application.dto;

import io.hhplus.tdd.point.api.domain.model.outport.UserPointInfo;
import io.hhplus.tdd.point.common.mapper.ObjectMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
public record UserPointSearchResponse(
	long id,
	long point,
	long updateMillis
) {
	public static UserPointSearchResponse from(UserPointInfo info) {
		return ObjectMapperBasedVoMapper.convert(info, UserPointSearchResponse.class);
	}
}
