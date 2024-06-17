package io.hhplus.tdd.cucumber.steps;


import static io.hhplus.tdd.cucumber.contextholder.ExceptionContextHolder.*;
import static io.hhplus.tdd.cucumber.contextholder.PointChargeContextHolder.*;
import static io.hhplus.tdd.cucumber.contextholder.PointSearchContextHolder.*;
import static io.hhplus.tdd.testhelpers.apiexecutor.PointApiExecutor.*;
import static io.hhplus.tdd.testhelpers.parser.PointChargeResponseParser.*;
import static io.hhplus.tdd.testhelpers.parser.PointSearchResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java8.En;
import io.cucumber.junit.platform.engine.Cucumber;
import io.hhplus.tdd.point.api.application.dto.UserPointSearchResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;


/**
 * 포인트 조회 기능에 대한 Cucumber Step 정의 클래스
 * 사용자의 포인트를 조회하고 결과를 검증합니다.
 *
 * @author : Rene Choi
 * @since : 2024/06/17
 */
public class PointSearchApiStepDef implements En {

	public PointSearchApiStepDef() {
		When("사용자의 id가 {long}인 경우 포인트를 조회 요청하고 정상 응답을 받는다", this::searchPointRequestedWithSuccessResponse);
		When("사용자의 id가 {long}인 경우 포인트를 조회 요청하면 예외가 발생한다", this::searchPointRequestedWithFailureResponse);
		Then("조회한 사용자의 포인트는 {long}이어야 한다", this::verifyUserPoints);
	}

	private void searchPointRequestedWithSuccessResponse(Long id) {
		ExtractableResponse<Response> response = searchPoint(id);
		assertEquals(200, response.statusCode());
		putUserPointSearchResponse(id,  parseUserPointSearchResponse(response));
	}

	private void searchPointRequestedWithFailureResponse(Long id) {
		assertNotEquals(200, searchPoint(id).statusCode());
	}

	private void verifyUserPoints(Long expectedPoints) {
		UserPointSearchResponse searchResponse = getMostRecentUserPointSearchResponse();
		assertEquals(expectedPoints, searchResponse.point(), "조회 후 포인트가 예상 값과 다릅니다.");
	}
}

