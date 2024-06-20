package io.hhplus.tdd.point.api.domain.model.inport;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */

public record UserPointChargeCommand(
	long id,
	long amount
) {
}
