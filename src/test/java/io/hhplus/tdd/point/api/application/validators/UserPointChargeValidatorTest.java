package io.hhplus.tdd.point.api.application.validators;

import static java.lang.System.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.hhplus.tdd.point.api.application.dto.UserPointChargeRequest;
import io.hhplus.tdd.point.api.application.specification.MaxPointsSpecificationFactory;
import io.hhplus.tdd.point.api.application.specification.PositiveAmountSpecification;
import io.hhplus.tdd.point.api.application.specification.Specification;
import io.hhplus.tdd.point.api.application.specification.ValidUserIdSpecification;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointSearchCommand;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointInfo;
import io.hhplus.tdd.point.api.domain.service.PointService;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
@ExtendWith(MockitoExtension.class)
class UserPointChargeValidatorTest {

	@Mock
	private PointService pointService;

	@Mock
	private ValidUserIdSpecification validUserIdSpecification;

	@Mock
	private PositiveAmountSpecification positiveAmountSpecification;

	@Mock
	private MaxPointsSpecificationFactory maxPointsSpecificationFactory;

	@InjectMocks
	private UserPointChargeValidator validator;

	private UserPointChargeRequest request;

	@BeforeEach
	void setUp() {
		request = new UserPointChargeRequest(1L, 100L);
	}

	@Test
	@DisplayName("유효한 요청에 대해 정상적으로 검증 통과")
	void validateValidRequestShouldPass() {
		// given
		UserPointInfo pointInfo = new UserPointInfo(1L, 200L, currentTimeMillis());
		Specification<UserPointChargeRequest> maxPointsSpecification = mock(Specification.class);

		when(pointService.search(any(UserPointSearchCommand.class))).thenReturn(pointInfo);
		when(maxPointsSpecificationFactory.specify(pointInfo.point())).thenReturn(maxPointsSpecification);
		when(maxPointsSpecification.isSatisfiedBy(request)).thenReturn(true);

		// when & then
		assertDoesNotThrow(() -> validator.validate(request));
	}

	@Test
	@DisplayName("유효하지 않은 사용자 ID에 대해 예외 발생")
	void validateInvalidUserIdShouldThrowException() {
		// given
		when(validUserIdSpecification.isNotSatisfiedBy(request)).thenReturn(true);

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(request));
		assertEquals("유효하지 않은 사용자 ID입니다", exception.getMessage());
	}

	@Test
	@DisplayName("음수 금액에 대해 예외 발생")
	void validateNegativeAmountShouldThrowException() {
		// given
		when(validUserIdSpecification.isNotSatisfiedBy(request)).thenReturn(false);
		when(positiveAmountSpecification.isNotSatisfiedBy(request)).thenReturn(true);

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(request));
		assertEquals("충전 금액은 양수여야 합니다", exception.getMessage());
	}

	@Test
	@DisplayName("최대 허용 포인트를 초과할 때 예외 발생")
	void validateExceedMaxPointsShouldThrowException() {
		// given
		UserPointInfo pointInfo = new UserPointInfo(1L, 900000L, currentTimeMillis());
		Specification<UserPointChargeRequest> maxPointsSpecification = mock(Specification.class);

		when(pointService.search(any(UserPointSearchCommand.class))).thenReturn(pointInfo);
		when(maxPointsSpecificationFactory.specify(pointInfo.point())).thenReturn(maxPointsSpecification);
		when(maxPointsSpecification.isSatisfiedBy(request)).thenReturn(false);

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(request));
		assertEquals("최대 허용 포인트를 초과합니다", exception.getMessage());
	}
}
