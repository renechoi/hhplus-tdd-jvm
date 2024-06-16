package io.hhplus.tdd.cucumber.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.hhplus.tdd.point.api.application.dto.UserPointChargeResponse;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public class PointChargeContextHolder {

	private static final ConcurrentHashMap<String, UserPointChargeResponse> userPointChargeResponseMap = new ConcurrentHashMap<>();
	private static final AtomicReference<Long> userId = new AtomicReference<>();
	private static final AtomicReference<Long> currentPoints = new AtomicReference<>();
	private static final AtomicReference<Long> chargeAmount = new AtomicReference<>();

	public static void initFields() {
		userPointChargeResponseMap.clear();
		userId.set(null);
		currentPoints.set(null);
		chargeAmount.set(null);
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

	public static void setChargeAmount(Long amount) {
		chargeAmount.set(amount);
	}

	public static Long getChargeAmount() {
		return chargeAmount.get();
	}

	public static void putUserPointChargeResponse(Long id, UserPointChargeResponse response) {
		userPointChargeResponseMap.put(String.valueOf(id), response);
	}

	public static UserPointChargeResponse getUserPointChargeResponse(Long id) {
		return userPointChargeResponseMap.get(String.valueOf(id));
	}

	public static UserPointChargeResponse getMostRecentUserPointChargeResponse() {
		return userPointChargeResponseMap.values().stream().reduce((first, second) -> second).orElse(null);
	}
}

