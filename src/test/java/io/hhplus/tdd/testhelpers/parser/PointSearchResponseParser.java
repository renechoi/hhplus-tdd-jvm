package io.hhplus.tdd.testhelpers.parser;

import io.hhplus.tdd.point.api.application.dto.UserPointSearchResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * 포인트 조회 응답을 파싱하는 유틸리티 클래스
 * 
 * @author : Rene Choi
 * @since : 2024/06/17
 */
public class PointSearchResponseParser {
    public static UserPointSearchResponse parseUserPointSearchResponse(ExtractableResponse<Response> response) {
        return response.as(UserPointSearchResponse.class);
    }
}
