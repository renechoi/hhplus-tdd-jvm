package io.hhplus.tdd.point.api.domain.service.impl;

import static io.hhplus.tdd.point.common.model.types.TransactionType.*;
import static java.lang.System.*;

import java.util.List;

import org.springframework.stereotype.Service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.api.domain.entity.PointHistory;
import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointChargeCommand;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointSearchCommand;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointUseCommand;
import io.hhplus.tdd.point.api.domain.model.outport.PointHistories;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointChargeInfo;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointInfo;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointUseInfo;
import io.hhplus.tdd.point.api.domain.service.PointService;
import io.hhplus.tdd.point.common.annotation.Transactional;
import io.hhplus.tdd.point.common.model.types.TransactionType;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@Service
@RequiredArgsConstructor
public class SimplePointService implements PointService {

	private final UserPointTable userPointTable;

	private final PointHistoryTable pointHistoryTable;

	@Override
	@Transactional
	public UserPointChargeInfo charge(UserPointChargeCommand chargeCommand) {
		UserPoint currentUserPoint = userPointTable.selectById(chargeCommand.id());
		UserPoint updatedUserPoint = userPointTable.insertOrUpdate(chargeCommand.id(), currentUserPoint.calculateNewPointWithSummation(chargeCommand.amount()));

		pointHistoryTable.insert(chargeCommand.id(), chargeCommand.amount(), CHARGE, currentTimeMillis());

		return UserPointChargeInfo.from(updatedUserPoint);
	}

	@Override
	public UserPointInfo search(UserPointSearchCommand searchCommand) {
		return UserPointInfo.from(userPointTable.selectById(searchCommand.id()));
	}

	@Override
	@Transactional
	public UserPointUseInfo use(UserPointUseCommand command) {
		UserPoint updatedUserPoint = userPointTable.selectById(command.id()).deductPoints(command.amount());
		UserPoint afterUseUserPoint = userPointTable.insertOrUpdate(updatedUserPoint.id(), updatedUserPoint.point());

		pointHistoryTable.insert(command.id(), command.amount(), TransactionType.USE, System.currentTimeMillis());

		return UserPointUseInfo.from(afterUseUserPoint);
	}

	@Override
	public PointHistories getHistories(long userId) {
		return PointHistories.from(pointHistoryTable.selectAllByUserId(userId));
	}
}
