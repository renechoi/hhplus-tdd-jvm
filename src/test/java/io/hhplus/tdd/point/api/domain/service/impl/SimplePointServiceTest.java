package io.hhplus.tdd.point.api.domain.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.hhplus.tdd.point.api.infrastructure.database.PointHistoryTable;
import io.hhplus.tdd.point.api.infrastructure.database.UserPointTable;
import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointChargeCommand;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointSearchCommand;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointUseCommand;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointChargeInfo;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointInfo;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointUseInfo;
import io.hhplus.tdd.point.common.model.types.TransactionType;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
@ExtendWith(MockitoExtension.class)
public class SimplePointServiceTest {

	@Mock
	private UserPointTable userPointTable;

	@Mock
	private PointHistoryTable pointHistoryTable;

	@InjectMocks
	private SimplePointService simplePointService;

	private UserPoint userPoint;

	@BeforeEach
	void setUp() {
		userPoint = new UserPoint(1L, 500L, System.currentTimeMillis());
	}


	@Test
	@DisplayName("포인트 충전 테스트 - 사용자의 포인트를 충전하고 충전 내역이 올바르게 기록되는지 확인")
	void testCharge() {
		// given
		UserPointChargeCommand chargeCommand = new UserPointChargeCommand(1L, 500L);
		when(userPointTable.selectById(1L)).thenReturn(userPoint);
		when(userPointTable.insertOrUpdate(eq(1L), anyLong())).thenReturn(new UserPoint(1L, 1000L, System.currentTimeMillis()));

		// when
		UserPointChargeInfo chargeInfo = simplePointService.charge(chargeCommand);

		// then
		assertEquals(1000L, chargeInfo.point(), "충전된 포인트가 잘못되었습니다.");

		ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
		verify(pointHistoryTable, times(1)).insert(eq(1L), eq(500L), eq(TransactionType.CHARGE), captor.capture());

		Long capturedMillis = captor.getValue();
		assertNotNull(capturedMillis, "캡처된 시간 값이 null입니다.");
		assertTrue(Math.abs(System.currentTimeMillis() - capturedMillis) < 1000, "캡처된 시간이 현재 시간과 너무 많이 차이납니다.");
	}

	@Test
	@DisplayName("포인트 조회 테스트 - 사용자의 포인트를 조회하고 조회 결과가 올바른지 확인")
	void testSearch() {
		// given
		UserPointSearchCommand searchCommand = new UserPointSearchCommand(1L, 300L);
		when(userPointTable.selectById(1L)).thenReturn(userPoint);

		// when
		UserPointInfo pointInfo = simplePointService.search(searchCommand);

		// then
		assertEquals(500L, pointInfo.point(), "조회된 포인트가 잘못되었습니다.");
	}

	@Test
	@DisplayName("포인트 사용 테스트 - 사용자의 포인트를 사용하고 사용 내역이 올바르게 기록되는지 확인")
	void testUse() {
		// given
		UserPointUseCommand useCommand = new UserPointUseCommand(1L, 300L);
		when(userPointTable.selectById(1L)).thenReturn(userPoint);
		when(userPointTable.insertOrUpdate(eq(1L), anyLong())).thenReturn(new UserPoint(1L, 200L, System.currentTimeMillis()));

		// when
		UserPointUseInfo useInfo = simplePointService.use(useCommand);

		// then
		assertEquals(200L, useInfo.point(), "사용된 후 남은 포인트가 잘못되었습니다.");

		ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
		verify(pointHistoryTable, times(1)).insert(eq(1L), eq(300L), eq(TransactionType.USE), captor.capture());

		Long capturedMillis = captor.getValue();
		assertNotNull(capturedMillis, "캡처된 시간 값이 null입니다.");
		assertTrue(Math.abs(System.currentTimeMillis() - capturedMillis) < 1000, "캡처된 시간이 현재 시간과 너무 많이 차이납니다.");
	}

	@Test
	@DisplayName("포인트 사용 실패 테스트 - 사용하려는 금액이 잔액보다 많을 때 예외가 발생하는지 확인")
	void testUse_InsufficientBalance() {
		// given
		UserPointUseCommand useCommand = new UserPointUseCommand(1L, 600L);
		when(userPointTable.selectById(1L)).thenReturn(userPoint);

		// when & then
		assertThrows(IllegalArgumentException.class, () -> simplePointService.use(useCommand), "잔액 부족 예외가 발생하지 않았습니다.");
	}
}