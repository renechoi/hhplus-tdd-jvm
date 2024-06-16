package io.hhplus.tdd.testhelpers.parser;

import io.hhplus.tdd.point.api.application.dto.UserPointChargeResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public class PointChargeResponseParser {
	public static UserPointChargeResponse parseUserPointChargeResponse(ExtractableResponse<Response> response) {
		return response.as(UserPointChargeResponse.class);
	}
}
