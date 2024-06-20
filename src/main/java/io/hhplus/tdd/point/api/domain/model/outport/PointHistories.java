package io.hhplus.tdd.point.api.domain.model.outport;

import java.util.List;

import io.hhplus.tdd.point.api.domain.entity.PointHistory;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
public record PointHistories(
	List<PointHistoryInfo> pointHistories
) {
	public static PointHistories from(List<PointHistory> pointHistories) {
		List<PointHistoryInfo> pointHistoryInfos = pointHistories.stream()
			.map(PointHistoryInfo::from)
			.toList();
		return new PointHistories(pointHistoryInfos);
	}
}