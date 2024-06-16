package io.hhplus.tdd.point.api.domain.service.impl;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointChargeCommand;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointChargeInfo;
import io.hhplus.tdd.point.common.transaction.UserPointTableProxy;

@SpringBootTest
@DisplayName("SimplePointService 트랜잭션 통합 테스트")
public class SimplePointServiceTransactionTest {

	@Autowired
	private SimplePointService simplePointService;

	@Autowired
	private UserPointTableProxy userPointTableProxy;

	@Autowired
	private UserPointTable userPointTable;

	@BeforeEach
	public void setUp() {
		// Given: 초기 데이터 설정
		userPointTable.insertOrUpdate(1L, 100L);
	}

	/**
	 * 유저 포인트 충전 시 트랜잭션이 성공적으로 커밋되는지 테스트합니다.
	 * 포인트 충전 후, 데이터베이스에 올바르게 반영되었는지 확인합니다.
	 */
	@Test
	@DisplayName("성공적인 트랜잭션 테스트")
	public void testSuccessfulTransaction() {
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
		// todo ->
	}
}
