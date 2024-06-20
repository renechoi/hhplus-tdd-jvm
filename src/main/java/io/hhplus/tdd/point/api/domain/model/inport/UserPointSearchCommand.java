package io.hhplus.tdd.point.api.domain.model.inport;

import io.hhplus.tdd.point.api.application.dto.UserPointChargeRequest;
import lombok.Builder;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@Builder
public record UserPointSearchCommand(
	long id,
	long amount
) {
	public static UserPointSearchCommand searchCommandById(UserPointChargeRequest request) {
		return UserPointSearchCommand.builder().id(request.getId()).build();
	}

	public static UserPointSearchCommand searchCommandById(long id) {
		return UserPointSearchCommand.builder().id(id).build();
	}
}
