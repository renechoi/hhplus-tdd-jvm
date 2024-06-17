package io.hhplus.tdd.point.api.application.specification;

import io.hhplus.tdd.point.api.application.dto.UserPointRequest;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public interface Specification<T extends UserPointRequest> {
	boolean isSatisfiedBy(T t);
}