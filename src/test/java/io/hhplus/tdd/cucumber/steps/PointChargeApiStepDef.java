package io.hhplus.tdd.cucumber.steps;

import static io.hhplus.tdd.cucumber.contextholder.PointChargeContextHolder.*;
import static io.hhplus.tdd.testhelpers.apiexecutor.PointApiExecutor.*;
import static io.hhplus.tdd.testhelpers.parser.PointChargeResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java8.En;
import io.hhplus.tdd.cucumber.contextholder.PointChargeContextHolder;
import io.hhplus.tdd.point.api.application.dto.UserPointChargeResponse;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public class PointChargeApiStepDef implements En {

	public PointChargeApiStepDef() {
		When("사용자의 id가 {long}이고 충전 금액이 {long}인 경우 포인트를 충전 요청하고 정상 응답을 받는다", this::chargePointRequestedWithSuccessResponse);
		When("사용자의 id가 {long}이고 추가 충전 금액이 {long}인 경우 포인트를 추가 충전 요청하고 정상 응답을 받는다", this::chargeAdditionalPointRequestedWithSuccessResponse);
		Then("사용자의 포인트는 {long}이어야 한다", this::verifyUserPoints);
	}


	private void chargePointRequestedWithSuccessResponse(Long id, Long amount) {
		setChargeAmount(amount);
		UserPointChargeResponse chargeResponse = parseUserPointChargeResponse(chargePointWithOk(id, amount));
		putUserPointChargeResponse(id, chargeResponse);
	}

	private void chargeAdditionalPointRequestedWithSuccessResponse(Long id, Long additionalAmount) {
		setChargeAmount(additionalAmount);
		UserPointChargeResponse chargeResponse = parseUserPointChargeResponse(chargePointWithOk(id, additionalAmount));
		putUserPointChargeResponse(id, chargeResponse);
	}

	private void verifyUserPoints(Long expectedPoints) {
		Long userId = getUserId();
		UserPointChargeResponse chargeResponse = getUserPointChargeResponse(userId);
		assertEquals(expectedPoints, chargeResponse.point(), "충전 후 포인트가 예상 값과 다릅니다.");
	}
}
