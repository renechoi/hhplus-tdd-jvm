package io.hhplus.tdd.testhelpers.parser;

import io.hhplus.tdd.point.api.application.dto.UserPointUseResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
public class PointUseResponseParser {
	public static UserPointUseResponse parseUserPointUseResponse(ExtractableResponse<Response> response) {
		return response.as(UserPointUseResponse.class);
	}
}