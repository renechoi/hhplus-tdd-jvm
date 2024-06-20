package io.hhplus.tdd.cucumber.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.hhplus.tdd.point.api.application.dto.PointHistoriesResponse;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
public class PointHistoryContextHolder {

	private static final ConcurrentHashMap<String, PointHistoriesResponse> pointHistoriesResponseMap = new ConcurrentHashMap<>();
	private static final AtomicReference<Long> mostRecentUserId = new AtomicReference<>();

	public static void initFields() {
		pointHistoriesResponseMap.clear();
		mostRecentUserId.set(null);
	}

	public static void putPointHistoriesResponse(Long id, PointHistoriesResponse response) {
		pointHistoriesResponseMap.put(String.valueOf(id), response);
		mostRecentUserId.set(id);
	}

	public static PointHistoriesResponse getPointHistoriesResponse(Long id) {
		return pointHistoriesResponseMap.get(String.valueOf(id));
	}

	public static PointHistoriesResponse getMostRecentPointHistoriesResponse() {
		Long recentUserId = mostRecentUserId.get();
		return recentUserId != null ? getPointHistoriesResponse(recentUserId) : null;
	}
}