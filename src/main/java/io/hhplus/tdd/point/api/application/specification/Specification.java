package io.hhplus.tdd.point.api.application.specification;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public interface Specification<T> {
	boolean isSatisfiedBy(T t);
}