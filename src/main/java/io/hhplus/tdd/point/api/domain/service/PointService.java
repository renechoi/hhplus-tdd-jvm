package io.hhplus.tdd.point.api.domain.service;

import io.hhplus.tdd.point.api.application.dto.UserPointChargeResponse;
import io.hhplus.tdd.point.api.domain.model.outport.UserPointChargeInfo;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public interface PointService {
	UserPointChargeInfo charge(long id, long amount);
}
