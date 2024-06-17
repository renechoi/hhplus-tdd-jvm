package io.hhplus.tdd.cucumber.steps;
import static io.hhplus.tdd.cucumber.contextholder.ExceptionContextHolder.*;
import static io.hhplus.tdd.cucumber.contextholder.PointUseContextHolder.*;
import static io.hhplus.tdd.testhelpers.apiexecutor.PointApiExecutor.*;
import static io.hhplus.tdd.testhelpers.parser.PointUseResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import io.cucumber.java8.En;
import io.hhplus.tdd.point.api.application.dto.UserPointUserRequest;
import io.hhplus.tdd.point.api.application.dto.UserPointUseResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */

public class PointUseApiStepDef implements En {

	public PointUseApiStepDef() {
		When("사용자의 id가 {long}이고 사용 금액이 {long}인 경우 포인트를 사용 요청하고 정상 응답을 받는다", this::usePointRequestedWithSuccessResponse);
		Then("사용자의 남은 포인트는 {long}이어야 한다", this::verifyUserPoints);

		// 예외 및 제약 조건
		When("사용자의 id가 {long}이고 사용 금액이 {long}인 경우 포인트를 사용 요청하면 예외가 발생한다", this::usePointRequestedWithFailureResponse);

		// 동시성 테스트
		When("사용자의 id가 {long}이고 동시에 사용 금액이 {long}인 요청을 {int}번 보낸다", this::usePointsConcurrently);
	}

	private void usePointRequestedWithSuccessResponse(Long id, Long amount) {
		setUseAmount(amount);
		UserPointUseResponse useResponse = parseUserPointUseResponse(usePointWithOk(new UserPointUserRequest(id, amount)));
		putUserPointUseResponse(id, useResponse);
	}

	private void verifyUserPoints(Long expectedPoints) {
		UserPointUseResponse useResponse = getMostRecentUserPointUseResponse();
		assertEquals(expectedPoints, useResponse.point(), "사용 후 포인트가 예상 값과 다릅니다.");
	}

	private void usePointRequestedWithFailureResponse(Long id, Long amount) {
		ExtractableResponse<Response> response = usePoint(new UserPointUserRequest(id, amount));
		assertNotEquals(200, response.statusCode());
	}

	private void usePointsConcurrently(Long id, Long amount, int times) {
		ExecutorService executorService = Executors.newFixedThreadPool(times);
		CountDownLatch latch = new CountDownLatch(times);

		IntStream.range(0, times).forEach(i -> {
			executorService.submit(() -> {
				awaitLatch(latch);
				usePointRequestedWithSuccessResponse(id, amount);
			});
			latch.countDown();
		});

		executorService.shutdown();
		awaitTermination(executorService, 10, TimeUnit.SECONDS);
	}

	@SneakyThrows
	private void awaitLatch(CountDownLatch latch) {
		latch.await(10, TimeUnit.SECONDS);
	}

	@SneakyThrows
	private void awaitTermination(ExecutorService executorService, long timeout, TimeUnit unit) {
		if (!executorService.awaitTermination(timeout, unit)) {
			executorService.shutdownNow();
		}
	}
}