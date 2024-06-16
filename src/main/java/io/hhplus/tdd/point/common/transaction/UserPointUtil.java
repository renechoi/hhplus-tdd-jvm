package io.hhplus.tdd.point.common.transaction;

import io.hhplus.tdd.point.api.domain.entity.UserPoint;

public class UserPointUtil {

    public static UserPoint copy(UserPoint userPoint) {
        return new UserPoint(userPoint.id(), userPoint.point(), userPoint.updateMillis());
    }
}
