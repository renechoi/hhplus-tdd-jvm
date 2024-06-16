package io.hhplus.tdd.point.api.application.specification;

import org.springframework.stereotype.Component;

import io.hhplus.tdd.point.api.application.dto.UserPointChargeRequest;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@Component
public class MaxPointsSpecificationFactory {

	private static final long MAX_POINTS = 1000000;

	public Specification<UserPointChargeRequest> specify(long currentPoints) {
		return request -> (currentPoints + request.getAmount()) <= MAX_POINTS;
	}
}