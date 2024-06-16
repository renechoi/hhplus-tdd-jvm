package io.hhplus.tdd.point.common.transaction;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import io.hhplus.tdd.point.util.UserPointUtil;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@Component
public class TransactionContext {

    private final ThreadLocal<Map<Long, UserPoint>> previousState = ThreadLocal.withInitial(HashMap::new);

    public void begin(Map<Long, UserPoint> currentState) {
        previousState.get().clear();
        currentState.forEach((id, userPoint) -> previousState.get().put(id, UserPointUtil.copy(userPoint)));
    }

    public void rollback(Map<Long, UserPoint> currentState) {
        currentState.clear();
        currentState.putAll(previousState.get());
    }

    public void commit() {
        previousState.get().clear();
    }
}
