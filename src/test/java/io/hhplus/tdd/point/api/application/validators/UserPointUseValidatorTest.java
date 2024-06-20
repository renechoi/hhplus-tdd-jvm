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

import io.hhplus.tdd.point.api.application.dto.UserPointUserRequest;
import io.hhplus.tdd.point.api.application.specification.PositiveAmountSpecification;
import io.hhplus.tdd.point.api.application.specification.ValidUserIdSpecification;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointSearchCommand;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointInfo;
import io.hhplus.tdd.point.api.domain.service.PointService;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
@ExtendWith(MockitoExtension.class)
class UserPointUseValidatorTest {

	@Mock
	private PointService pointService;

	@Mock
	private ValidUserIdSpecification validUserIdSpecification;

	@Mock
	private PositiveAmountSpecification positiveAmountSpecification;

	@InjectMocks
	private UserPointUseValidator validator;

	private UserPointUserRequest request;

	@BeforeEach
	void setUp() {
		request = new UserPointUserRequest(1L, 100L);
	}

	@Test
	@DisplayName("유효한 요청에 대해 정상적으로 검증 통과")
	void validateValidRequestShouldPass() {
		// given
		UserPointInfo pointInfo = new UserPointInfo(1L, 200L, currentTimeMillis());

		when(pointService.search(any(UserPointSearchCommand.class))).thenReturn(pointInfo);

		// when & then
		assertDoesNotThrow(() -> validator.validate(request));
	}

	@Test
	@DisplayName("유효하지 않은 사용자 ID에 대한 예외 발생")
	void validateInvalidUserIdShouldThrowException() {
		// given
		when(validUserIdSpecification.isNotSatisfiedBy(request)).thenReturn(true);

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(request));
		assertEquals("유효하지 않은 사용자 ID입니다", exception.getMessage());
	}

	@Test
	@DisplayName("음수 금액에 대한 예외 발생")
	void validateNegativeAmountShouldThrowException() {
		// given
		when(validUserIdSpecification.isNotSatisfiedBy(request)).thenReturn(false);
		when(positiveAmountSpecification.isNotSatisfiedBy(request)).thenReturn(true);

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(request));
		assertEquals("사용 금액은 양수여야 합니다", exception.getMessage());
	}

	@Test
	@DisplayName("사용하려는 포인트가 현재 포인트보다 많을 때 예외 발생")
	void validateInsufficientPointsShouldThrowException() {
		// given
		UserPointInfo pointInfo = new UserPointInfo(1L, 50L, currentTimeMillis());

		when(pointService.search(any(UserPointSearchCommand.class))).thenReturn(pointInfo);

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(request));
		assertEquals("사용하려는 포인트가 현재 포인트보다 많습니다", exception.getMessage());
	}
}
