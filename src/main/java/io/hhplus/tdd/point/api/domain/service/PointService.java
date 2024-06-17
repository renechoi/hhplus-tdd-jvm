package io.hhplus.tdd.point.api.domain.service;

import io.hhplus.tdd.point.api.domain.model.outport.PointHistories;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointChargeCommand;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointSearchCommand;
import io.hhplus.tdd.point.api.domain.model.inport.UserPointUseCommand;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointChargeInfo;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointInfo;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointUseInfo;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public interface PointService {
	UserPointChargeInfo charge(UserPointChargeCommand userPointChargeCommand);
	UserPointInfo search(UserPointSearchCommand userPointSearchCommand);
	UserPointUseInfo use(UserPointUseCommand command);
	PointHistories getHistories(long userId);
}
