package io.hhplus.tdd.point.api.domain.entity;

import static java.lang.System.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
class UserPointTest {


	@Test
	@DisplayName("포인트 계산 검증 - 주어진 포인트에 더해진 포인트가 올바르게 계산되는지 확인")
	void testCalculateNewPointWithSummation() {
		UserPoint userPoint = new UserPoint(1L, 500L, currentTimeMillis());
		long newPoints = userPoint.calculateNewPointWithSummation(500L);
		assertEquals(1000L, newPoints, "포인트 계산이 잘못되었습니다.");
	}

	@Test
	@DisplayName("포인트 차감 검증 - 사용자가 포인트를 사용한 후 남은 포인트가 올바르게 계산되는지 확인")
	void testDeductPoints() {
		UserPoint userPoint = new UserPoint(1L, 500L, currentTimeMillis());
		UserPoint updatedUserPoint = userPoint.deductPoints(300L);
		assertEquals(200L, updatedUserPoint.point(), "포인트 차감이 잘못되었습니다.");
	}

	@Test
	@DisplayName("잔액 부족 예외 검증 - 사용자가 보유한 포인트보다 많은 포인트를 사용하려 할 때 예외가 발생하는지 확인")
	void testDeductPoints_InsufficientBalance() {
		UserPoint userPoint = new UserPoint(1L, 500L, currentTimeMillis());
		assertThrows(IllegalArgumentException.class, () -> userPoint.deductPoints(600L), "잔액 부족 예외가 발생하지 않았습니다.");
	}

	@Test
	@DisplayName("빈 사용자 포인트 객체 생성 검증 - ID가 주어졌을 때 초기 포인트가 0인 빈 UserPoint 객체가 올바르게 생성되는지 확인")
	void testEmptyUserPoint() {
		UserPoint emptyUserPoint = UserPoint.empty(1L);
		assertEquals(1L, emptyUserPoint.id(), "ID가 잘못되었습니다.");
		assertEquals(0L, emptyUserPoint.point(), "초기 포인트가 잘못되었습니다.");
	}
}