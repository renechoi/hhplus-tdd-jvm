package io.hhplus.tdd.point.api.domain.service.impl;

import static io.hhplus.tdd.point.common.model.types.TransactionType.*;
import static java.lang.System.*;

import org.springframework.stereotype.Service;

import io.hhplus.tdd.point.api.infrastructure.database.PointHistoryTable;
import io.hhplus.tdd.point.api.infrastructure.database.UserPointTable;
import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointChargeCommand;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointSearchCommand;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointUseCommand;
import io.hhplus.tdd.point.api.domain.model.outport.PointHistories;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointChargeInfo;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointInfo;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointUseInfo;
import io.hhplus.tdd.point.api.domain.service.PointService;
import io.hhplus.tdd.point.api.infrastructure.persistence.PointHistoryRepository;
import io.hhplus.tdd.point.api.infrastructure.persistence.UserPointRepository;
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

	private final UserPointRepository userPointRepository;
	private final PointHistoryRepository pointHistoryRepository;

	@Override
	@Transactional
	public UserPointChargeInfo charge(UserPointChargeCommand chargeCommand) {
		UserPoint currentUserPoint = userPointRepository.selectById(chargeCommand.id());
		UserPoint updatedUserPoint = userPointRepository.insertOrUpdate(chargeCommand.id(), currentUserPoint.calculateNewPointWithSummation(chargeCommand.amount()));

		pointHistoryRepository.insert(chargeCommand.id(), chargeCommand.amount(), CHARGE, currentTimeMillis());

		return UserPointChargeInfo.from(updatedUserPoint);
	}

	@Override
	public UserPointInfo search(UserPointSearchCommand searchCommand) {
		return UserPointInfo.from(userPointRepository.selectById(searchCommand.id()));
	}

	@Override
	@Transactional
	public UserPointUseInfo use(UserPointUseCommand command) {
		UserPoint updatedUserPoint = userPointRepository.selectById(command.id()).deductPoints(command.amount());
		UserPoint afterUseUserPoint = userPointRepository.insertOrUpdate(updatedUserPoint.id(), updatedUserPoint.point());

		pointHistoryRepository.insert(command.id(), command.amount(), TransactionType.USE, System.currentTimeMillis());

		return UserPointUseInfo.from(afterUseUserPoint);
	}

	@Override
	public PointHistories getHistories(long userId) {
		return PointHistories.from(pointHistoryRepository.selectAllByUserId(userId));
	}
}
