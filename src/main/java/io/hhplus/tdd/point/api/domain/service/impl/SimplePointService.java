package io.hhplus.tdd.point.api.domain.service.impl;

import org.springframework.stereotype.Service;

import io.hhplus.tdd.database.UserPointTable;
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
	public UserPointChargeInfo charge(long id, long amount) {

		// todo charge


		return null;
	}
}
