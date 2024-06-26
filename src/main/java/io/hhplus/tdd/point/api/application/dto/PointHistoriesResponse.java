package io.hhplus.tdd.point.api.application.dto;

import java.util.List;

import io.hhplus.tdd.point.api.domain.model.outport.PointHistories;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
public record PointHistoriesResponse(
	List<PointHistoryResponse> pointHistories
) {
	public static PointHistoriesResponse from(PointHistories pointHistories) {
		List<PointHistoryResponse> pointHistoryResponses = pointHistories.pointHistories().stream()
			.map(PointHistoryResponse::from)
			.toList();
		return new PointHistoriesResponse(pointHistoryResponses);
	}
}