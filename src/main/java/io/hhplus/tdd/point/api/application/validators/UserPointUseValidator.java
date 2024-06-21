package io.hhplus.tdd.point.api.application.validators;

import org.springframework.stereotype.Component;

import io.hhplus.tdd.point.api.application.dto.UserPointUserRequest;
import io.hhplus.tdd.point.api.application.specification.PositiveAmountSpecification;
import io.hhplus.tdd.point.api.application.specification.ValidUserIdSpecification;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointSearchCommand;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointInfo;
import io.hhplus.tdd.point.api.domain.service.PointService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
@Component
@RequiredArgsConstructor
public class UserPointUseValidator implements Validator<UserPointUserRequest> {

	private final PointService pointService;
	private final ValidUserIdSpecification validUserIdSpecification;
	private final PositiveAmountSpecification positiveAmountSpecification;

	@Override
	public void validate(UserPointUserRequest request) {
		UserPointInfo pointInfo = pointService.search(UserPointSearchCommand.searchCommandById(request.getId()));

		if (validUserIdSpecification.isNotSatisfiedBy(request)) {
			throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다");
		}
		if (positiveAmountSpecification.isNotSatisfiedBy(request)) {
			throw new IllegalArgumentException("사용 금액은 양수여야 합니다");
		}
		if (request.getAmount() > pointInfo.point()) {
			throw new IllegalArgumentException("사용하려는 포인트가 현재 포인트보다 많습니다");
		}
	}
}