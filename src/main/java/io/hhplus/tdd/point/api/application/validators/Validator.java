package io.hhplus.tdd.point.api.application.validators;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public interface Validator<T> {
	void validate(T t);
}