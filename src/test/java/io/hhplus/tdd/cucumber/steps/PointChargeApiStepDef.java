package io.hhplus.tdd.cucumber.steps;

import static io.hhplus.tdd.cucumber.contextholder.ExceptionContextHolder.*;
import static io.hhplus.tdd.cucumber.contextholder.PointChargeContextHolder.*;
import static io.hhplus.tdd.testhelpers.apiexecutor.PointApiExecutor.*;
import static io.hhplus.tdd.testhelpers.parser.PointChargeResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;

import io.cucumber.java8.En;
import io.hhplus.tdd.cucumber.contextholder.PointChargeContextHolder;
import io.hhplus.tdd.point.api.application.dto.UserPointChargeRequest;
import io.hhplus.tdd.point.api.application.dto.UserPointChargeResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public class PointChargeApiStepDef implements En {

	public PointChargeApiStepDef() {
		When("사용자의 id가 {long}이고 충전 금액이 {long}인 경우 포인트를 충전 요청하고 정상 응답을 받는다", this::chargePointRequestedWithSuccessResponse);
		When("사용자의 id가 {long}이고 추가 충전 금액이 {long}인 경우 포인트를 추가 충전 요청하고 정상 응답을 받는다", this::chargeAdditionalPointRequestedWithSuccessResponse);
		Then("사용자의 포인트는 {long}이어야 한다", this::verifyUserPoints);

		// 예외 및 제약 조건
		When("사용자의 id가 {long}이고 충전 금액이 {long}인 경우 포인트를 충전 요청하면 예외가 발생한다", this::chargePointRequestedWithFailureResponse);
		When("{long} 포인트를 충전 요청하면 예외가 발생한다", this::chargeAmountCausesException);
	}




	private void chargePointRequestedWithSuccessResponse(Long id, Long amount) {
		setChargeAmount(amount);
		UserPointChargeResponse chargeResponse = parseUserPointChargeResponse(chargePointWithOk(new UserPointChargeRequest(id, amount)));
		putUserPointChargeResponse(id, chargeResponse);
	}

	private void chargeAdditionalPointRequestedWithSuccessResponse(Long id, Long additionalAmount) {
		setChargeAmount(additionalAmount);
		UserPointChargeResponse chargeResponse = parseUserPointChargeResponse(chargePointWithOk(new UserPointChargeRequest(id, additionalAmount)));
		putUserPointChargeResponse(id, chargeResponse);
	}

	private void verifyUserPoints(Long expectedPoints) {
		UserPointChargeResponse chargeResponse = getMostRecentUserPointChargeResponse();
		assertEquals(expectedPoints, chargeResponse.point(), "충전 후 포인트가 예상 값과 다릅니다.");
	}



	private void chargePointRequestedWithFailureResponse(Long id, Long amount) {
		ExtractableResponse<Response> response = chargePoint(new UserPointChargeRequest(id, amount));
		assertNotEquals(200,response.statusCode());
	}

	private void chargeAmountCausesException(Long amount) {
		ExtractableResponse<Response> response = chargePoint(new UserPointChargeRequest(getMostRecentUserPointChargeResponse().id(), amount));
		assertNotEquals(200,response.statusCode());
	}

}
