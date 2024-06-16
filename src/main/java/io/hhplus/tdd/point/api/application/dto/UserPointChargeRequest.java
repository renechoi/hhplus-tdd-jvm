package io.hhplus.tdd.point.api.application.dto;

import io.hhplus.tdd.point.api.domain.model.inport.UserPointChargeCommand;
import io.hhplus.tdd.point.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@Data
@AllArgsConstructor
public class UserPointChargeRequest {
	private long id;
	private long amount;

	public UserPointChargeRequest withId(long id) {
		this.id = id;
		return this;
	}

	public UserPointChargeCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, UserPointChargeCommand.class);
	}
}
