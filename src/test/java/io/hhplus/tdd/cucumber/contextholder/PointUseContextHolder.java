package io.hhplus.tdd.cucumber.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.hhplus.tdd.point.api.application.dto.UserPointUseResponse;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */

public class PointUseContextHolder {

	private static final ConcurrentHashMap<String, UserPointUseResponse> userPointUseResponseMap = new ConcurrentHashMap<>();
	private static final AtomicReference<Long> userId = new AtomicReference<>();
	private static final AtomicReference<Long> currentPoints = new AtomicReference<>();
	private static final AtomicReference<Long> useAmount = new AtomicReference<>();
	private static final AtomicReference<Long> mostRecentUserId = new AtomicReference<>();

	public static void initFields() {
		userPointUseResponseMap.clear();
		userId.set(null);
		currentPoints.set(null);
		useAmount.set(null);
		mostRecentUserId.set(null);
	}

	public static void setUserId(Long id) {
		userId.set(id);
	}

	public static Long getUserId() {
		return userId.get();
	}

	public static void setCurrentPoints(Long points) {
		currentPoints.set(points);
	}

	public static Long getCurrentPoints() {
		return currentPoints.get();
	}

	public static void setUseAmount(Long amount) {
		useAmount.set(amount);
	}

	public static Long getUseAmount() {
		return useAmount.get();
	}

	public static void putUserPointUseResponse(Long id, UserPointUseResponse response) {
		userPointUseResponseMap.put(String.valueOf(id), response);
		mostRecentUserId.set(id);
	}

	public static UserPointUseResponse getUserPointUseResponse(Long id) {
		return userPointUseResponseMap.get(String.valueOf(id));
	}

	public static UserPointUseResponse getMostRecentUserPointUseResponse() {
		Long recentUserId = mostRecentUserId.get();
		return recentUserId != null ? getUserPointUseResponse(recentUserId) : null;
	}
}