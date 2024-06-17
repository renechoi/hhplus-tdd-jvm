package io.hhplus.tdd.point.api.application.specification;

import org.springframework.stereotype.Component;

import io.hhplus.tdd.point.api.application.dto.UserPointChargeRequest;
import io.hhplus.tdd.point.api.application.dto.UserPointRequest;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@Component
public class PositiveAmountSpecification implements Specification<UserPointRequest> {

	@Override
	public boolean isSatisfiedBy(UserPointRequest request) {
		return request.getAmount() > 0;
	}
}