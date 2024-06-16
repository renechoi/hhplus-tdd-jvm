package io.hhplus.tdd.point.api.domain.service.impl;

import org.springframework.stereotype.Service;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointChargeCommand;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointSearchCommand;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointChargeInfo;
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
	public UserPointChargeInfo search(UserPointSearchCommand searchCommand) {
		return UserPointChargeInfo.from(userPointTable.selectById(searchCommand.id()));
	}
}
