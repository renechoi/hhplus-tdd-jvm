package io.hhplus.tdd.point.api.application.dto;

import io.hhplus.tdd.point.api.domain.model.inport.UserPointChargeCommand;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointUseCommand;
import io.hhplus.tdd.point.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
@Data
@AllArgsConstructor
public class UserPointUserRequest implements UserPointRequest{

	private long id;
	private long amount;

	public UserPointUserRequest withId(long id) {
		this.id = id;
		return this;
	}

	public UserPointUseCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, UserPointUseCommand.class);
	}
}
