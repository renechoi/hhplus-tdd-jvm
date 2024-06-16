package io.hhplus.tdd.point.api.application.facade;

import io.hhplus.tdd.point.api.application.dto.UserPointChargeRequest;
import io.hhplus.tdd.point.api.application.dto.UserPointChargeResponse;
import io.hhplus.tdd.point.api.application.validators.UserPointChargeValidator;
import io.hhplus.tdd.point.api.domain.service.PointService;
import io.hhplus.tdd.point.common.annotation.Facade;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@Facade
@RequiredArgsConstructor
public class PointFacade {

	private final PointService pointService;
	private final UserPointChargeValidator validator;

	public UserPointChargeResponse charge(UserPointChargeRequest chargeRequest) {

		validator.validate(chargeRequest);

		return UserPointChargeResponse.from(pointService.charge(chargeRequest.toCommand()));
	}
}
