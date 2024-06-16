package io.hhplus.tdd.point.api.application.validators;

import org.springframework.stereotype.Component;

import io.hhplus.tdd.point.api.application.dto.UserPointChargeRequest;
import io.hhplus.tdd.point.api.application.specification.MaxPointsSpecificationFactory;
import io.hhplus.tdd.point.api.application.specification.PositiveAmountSpecification;
import io.hhplus.tdd.point.api.application.specification.ValidUserIdSpecification;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointSearchCommand;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointChargeInfo;
import io.hhplus.tdd.point.api.domain.service.PointService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@Component
@RequiredArgsConstructor
public class UserPointChargeValidator implements Validator<UserPointChargeRequest> {

	private final PointService pointService;
	private final ValidUserIdSpecification validUserIdSpecification;
	private final PositiveAmountSpecification positiveAmountSpecification;
	private final MaxPointsSpecificationFactory maxPointsSpecificationFactory;

	@Override
	public void validate(UserPointChargeRequest request) {
		UserPointChargeInfo pointInfo = pointService.search(UserPointSearchCommand.searchCommandById(request));

		if (!validUserIdSpecification.isSatisfiedBy(request)) {
			throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다");
		}
		if (!positiveAmountSpecification.isSatisfiedBy(request)) {
			throw new IllegalArgumentException("충전 금액은 양수여야 합니다");
		}
		if (!maxPointsSpecificationFactory.specify(pointInfo.point()).isSatisfiedBy(request)) {
			throw new IllegalArgumentException("최대 허용 포인트를 초과합니다");
		}
	}
}