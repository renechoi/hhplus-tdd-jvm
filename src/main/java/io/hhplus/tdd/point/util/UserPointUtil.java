package io.hhplus.tdd.point.util;

import io.hhplus.tdd.point.api.domain.entity.UserPoint;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public class UserPointUtil {

    public static UserPoint copy(UserPoint userPoint) {
        return new UserPoint(userPoint.id(), userPoint.point(), userPoint.updateMillis());
    }
}
