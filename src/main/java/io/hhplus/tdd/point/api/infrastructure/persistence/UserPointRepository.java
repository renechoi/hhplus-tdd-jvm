package io.hhplus.tdd.point.api.infrastructure.persistence;

import io.hhplus.tdd.point.api.domain.entity.UserPoint;

/**
 * @author : Rene Choi
 * @since : 2024/06/19
 */
public interface UserPointRepository {
	UserPoint selectById(Long id);
	UserPoint insertOrUpdate(long id, long amount);
}