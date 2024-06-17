package io.hhplus.tdd.point.api.application.facade;

import static io.hhplus.tdd.point.api.domain.model.inport.UserPointSearchCommand.*;

import io.hhplus.tdd.point.api.application.dto.UserPointChargeRequest;
import io.hhplus.tdd.point.api.application.dto.UserPointChargeResponse;
import io.hhplus.tdd.point.api.application.dto.UserPointSearchResponse;
import io.hhplus.tdd.point.api.application.validators.UserPointChargeValidator;
import io.hhplus.tdd.point.api.domain.service.PointService;
import io.hhplus.tdd.point.common.annotation.DistributedLock;
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

	@DistributedLock(prefix = "charge", keys = {"chargeRequest.id"})
	public UserPointChargeResponse charge(UserPointChargeRequest chargeRequest) {

		validator.validate(chargeRequest);

		return UserPointChargeResponse.from(pointService.charge(chargeRequest.toCommand()));
	}

	public UserPointSearchResponse search(long id) {
		return UserPointSearchResponse.from(pointService.search(searchCommandById(id)));
	}
}
