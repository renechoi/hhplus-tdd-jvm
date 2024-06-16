package io.hhplus.tdd.point.api.application.facade;

import io.hhplus.tdd.point.api.application.dto.UserPointChargeResponse;
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

	public UserPointChargeResponse charge(long id, long amount) {
		return UserPointChargeResponse.from(pointService.charge(id, amount));
	}
}
