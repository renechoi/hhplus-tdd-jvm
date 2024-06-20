package io.hhplus.tdd.cucumber.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import io.hhplus.tdd.point.api.application.dto.UserPointSearchResponse;

/**
 * 포인트 조회 요청에 대한 컨텍스트 홀더
 * 조회 요청의 응답을 저장하고 관리합니다.
 *
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public class PointSearchContextHolder {

	private static final ConcurrentHashMap<String, UserPointSearchResponse> userPointSearchResponseMap = new ConcurrentHashMap<>();
	private static final AtomicReference<Long> userId = new AtomicReference<>();
	private static final AtomicReference<Long> mostRecentUserId = new AtomicReference<>();


	public static void initFields() {
		userPointSearchResponseMap.clear();
		userId.set(null);
		mostRecentUserId.set(null);
	}

	public static void setUserId(Long id) {
		userId.set(id);
	}

	public static Long getUserId() {
		return userId.get();
	}

	public static void putUserPointSearchResponse(Long id, UserPointSearchResponse response) {
		userPointSearchResponseMap.put(String.valueOf(id), response);
		mostRecentUserId.set(id);
	}

	public static UserPointSearchResponse getUserPointSearchResponse(Long id) {
		return userPointSearchResponseMap.get(String.valueOf(id));
	}

	public static UserPointSearchResponse getMostRecentUserPointSearchResponse() {
		Long recentUserId = mostRecentUserId.get();
		return recentUserId != null ? getUserPointSearchResponse(recentUserId) : null;
	}
}

