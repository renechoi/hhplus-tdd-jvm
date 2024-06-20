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

    public UserPoint deductPoints(long amount) {
        if (amount > this.point) {
            throw new IllegalArgumentException("잔액이 충분하지 않습니다.");
        }
        return new UserPoint(this.id, this.point - amount, System.currentTimeMillis());
    }
}
