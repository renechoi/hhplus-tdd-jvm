package io.hhplus.tdd.point.api.domain.entity;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public long calculateNewPointWithSummation(long addingAmount) {
        return this.point+addingAmount;
    }
}