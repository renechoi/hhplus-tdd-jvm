package io.hhplus.tdd.point.api.domain.service.impl;

import org.springframework.stereotype.Service;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointChargeCommand;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointSearchCommand;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointUseCommand;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointChargeInfo;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointInfo;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointUseInfo;
import io.hhplus.tdd.point.api.domain.service.PointService;
import io.hhplus.tdd.point.common.annotation.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@Service
@RequiredArgsConstructor
public class SimplePointService implements PointService {

	private final UserPointTable userPointTable;

	@Override
	@Transactional
	public UserPointChargeInfo charge(UserPointChargeCommand chargeCommand) {
		UserPoint currentUserPoint = userPointTable.selectById(chargeCommand.id());

		long newPointAmount = currentUserPoint.calculateNewPointWithSummation(chargeCommand.amount());

		UserPoint updatedUserPoint = userPointTable.insertOrUpdate(chargeCommand.id(), newPointAmount);

		return UserPointChargeInfo.from(updatedUserPoint);
	}

	@Override
	public UserPointInfo search(UserPointSearchCommand searchCommand) {
		return UserPointInfo.from(userPointTable.selectById(searchCommand.id()));
	}

	@Override
	@Transactional
	public UserPointUseInfo use(UserPointUseCommand command) {
		// 현재 포인트 조회
		UserPoint currentUserPoint = userPointTable.selectById(command.id());

		// 포인트 차감 후 업데이트
		UserPoint updatedUserPoint = currentUserPoint.deductPoints(command.amount());
		return UserPointUseInfo.from(userPointTable.insertOrUpdate(updatedUserPoint.id(), updatedUserPoint.point()));
	}
}
