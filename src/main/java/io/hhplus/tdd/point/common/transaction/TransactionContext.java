package io.hhplus.tdd.point.common.transaction;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import io.hhplus.tdd.point.api.domain.entity.UserPoint;

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
