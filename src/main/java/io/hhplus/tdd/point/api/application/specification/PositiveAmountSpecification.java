package io.hhplus.tdd.point.api.application.specification;

import org.springframework.stereotype.Component;

import io.hhplus.tdd.point.api.application.dto.UserPointChargeRequest;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@Component
public class PositiveAmountSpecification implements Specification<UserPointChargeRequest> {

	@Override
	public boolean isSatisfiedBy(UserPointChargeRequest request) {
		return request.getAmount() > 0;
	}
}