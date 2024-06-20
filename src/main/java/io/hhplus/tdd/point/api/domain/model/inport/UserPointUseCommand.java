package io.hhplus.tdd.point.api.domain.model.inport;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */

public record UserPointUseCommand(
	long id,
	long amount
) {
}
