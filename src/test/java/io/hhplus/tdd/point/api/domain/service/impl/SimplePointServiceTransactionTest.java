package io.hhplus.tdd.point.api.domain.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import io.hhplus.tdd.point.api.infrastructure.database.PointHistoryTable;
import io.hhplus.tdd.point.api.infrastructure.database.UserPointTable;
import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointChargeCommand;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointChargeInfo;
import io.hhplus.tdd.point.common.model.types.TransactionType;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
@SpringBootTest
@DisplayName("SimplePointService 트랜잭션 통합 테스트")
public class SimplePointServiceTransactionTest {

	@Autowired
	private SimplePointService simplePointService;

	@Autowired
	private UserPointTable userPointTable;

	@MockBean
	private PointHistoryTable pointHistoryTable;

	@AfterEach
	void databaseClear(){
		userPointTable.insertOrUpdate(1L,0L);
	}


	/**
	 * 유저 포인트 충전 시 트랜잭션이 성공적으로 커밋되는지 테스트합니다.
	 * 포인트 충전 후, 데이터베이스에 올바르게 반영되었는지 확인합니다.
	 */
	@Test
	@DisplayName("성공적인 트랜잭션 테스트")
	public void testSuccessfulTransaction() {
		// Given: 초기 데이터 설정
		userPointTable.insertOrUpdate(1L, 100L);

		// When: 유저 포인트를 충전
		UserPointChargeCommand command = new UserPointChargeCommand(1L, 50L);
		UserPointChargeInfo chargeInfo = simplePointService.charge(command);

		// Then: 트랜잭션이 성공적으로 커밋되었는지 확인
		assertThat(chargeInfo).isNotNull();
		assertThat(chargeInfo.point()).isEqualTo(150L);

		UserPoint userPoint = userPointTable.selectById(1L);
		assertThat(userPoint.point()).isEqualTo(150L);
	}

	/**
	 * 유저 포인트 충전 시 예외가 발생하면 트랜잭션이 롤백되는지 테스트합니다.
	 * 예외 발생 후, 데이터베이스가 원래 상태로 복구되었는지 확인합니다.
	 */
	@Test
	@DisplayName("트랜잭션 롤백 테스트")
	public void testRollbackTransaction() {
		// Given: 초기 데이터 설정
		UserPointChargeCommand command = new UserPointChargeCommand(1L, 100L);
		simplePointService.charge(command);

		// When: PointHistoryTable의 insert 메서드가 예외를 던지도록 설정
		doThrow(new RuntimeException("Test Exception"))
			.when(pointHistoryTable)
			.insert(eq(1L), eq(50L), eq(TransactionType.CHARGE), anyLong());


		// When: 유저 포인트를 충전 시도
		command = new UserPointChargeCommand(1L, 50L);
		try {
			simplePointService.charge(command);
		} catch (RuntimeException ignored) {}

		// Then: 트랜잭션이 롤백되었는지 확인
		UserPoint userPoint = userPointTable.selectById(1L);
		assertThat(userPoint.point()).isEqualTo(100L);
	}
}
