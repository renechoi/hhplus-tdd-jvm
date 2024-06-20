package io.hhplus.tdd.point.common.transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import io.hhplus.tdd.point.util.UserPointUtil;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@Component
public class TransactionContext {

    private final Map<Long, UserPoint> previousState = new ConcurrentHashMap<>();

    public void begin(Map<Long, UserPoint> currentState) {
        previousState.clear();
        currentState.forEach((id, userPoint) -> previousState.put(id, UserPointUtil.copy(userPoint)));
    }

    public void rollback(Map<Long, UserPoint> currentState) {
        currentState.clear();
        currentState.putAll(previousState);
        previousState.clear();
    }

    public void commit() {
        previousState.clear();
    }

    public Map<Long, UserPoint> getPreviousState() {
        return new ConcurrentHashMap<>(previousState);
    }
}