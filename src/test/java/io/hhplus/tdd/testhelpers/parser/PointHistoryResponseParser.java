package io.hhplus.tdd.testhelpers.parser;

import io.hhplus.tdd.point.api.application.dto.PointHistoriesResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
public class PointHistoryResponseParser {
	public static PointHistoriesResponse parsePointHistoriesResponse(ExtractableResponse<Response> response) {
		return response.as(PointHistoriesResponse.class);
	}
}