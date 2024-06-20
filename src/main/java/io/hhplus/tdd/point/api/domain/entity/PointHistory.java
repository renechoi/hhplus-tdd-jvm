package io.hhplus.tdd.point.api.domain.entity;

import io.hhplus.tdd.point.common.model.types.TransactionType;

public record PointHistory(
        long id,
        long userId,
        long amount,
        TransactionType type,
        long updateMillis
) {
}
