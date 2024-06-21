package io.hhplus.tdd.cucumber.steps;

import static io.hhplus.tdd.cucumber.contextholder.PointHistoryContextHolder.*;
import static io.hhplus.tdd.testhelpers.apiexecutor.PointApiExecutor.*;
import static io.hhplus.tdd.testhelpers.parser.PointHistoryResponseParser.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import io.hhplus.tdd.point.api.application.dto.PointHistoriesResponse;
import io.hhplus.tdd.point.api.application.dto.PointHistoryResponse;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */

public class PointHistoryApiStepDef implements En {

	public PointHistoryApiStepDef() {
		When("사용자의 id가 {long}인 경우 포인트 이력을 조회 요청하고 정상 응답을 받는다", this::historyPointRequestedWithSuccessResponse);
		Then("조회한 사용자의 포인트 이력은 다음과 같이 확인되어야 한다", this::verifyHistoryContains);
		Then("조회한 사용자의 포인트 이력은 비어있어야 한다", this::verifyHistoryIsEmpty);
	}

	private void historyPointRequestedWithSuccessResponse(Long id) {
		putPointHistoriesResponse(id, parsePointHistoriesResponse(getHistoriesWithOk(id)));
	}

	/**
	 * 조회한 사용자의 포인트 이력이 예상된 값과 일치하는지 검증합니다.
	 * <p>
	 * 이 메서드는 Cucumber의 DataTable을 사용하여 테스트 시나리오에서 기대하는 포인트 이력 데이터를 받아옵니다.
	 * 그런 다음 실제 응답에서 반환된 포인트 이력 데이터와 비교하여 일치 여부를 확인합니다.
	 *
	 * @param dataTable 예상된 포인트 이력 값이 포함된 DataTable
	 */
	private void verifyHistoryContains(DataTable dataTable) {
		PointHistoriesResponse historiesResponse = getMostRecentPointHistoriesResponse();
		List<PointHistoryResponse> actualHistories = historiesResponse.pointHistories();

		List<Map<String, String>> expectedHistories = dataTable.asMaps(String.class, String.class);

		for (Map<String, String> expectedHistory : expectedHistories) {
			boolean matchFound = actualHistories.stream().anyMatch(actualHistory -> {
				try {
					return matchHistory(expectedHistory, actualHistory);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			});
			assertTrue(matchFound, "기대한 이력이 발견되지 않았습니다: " + expectedHistory);
		}
	}

	/**
	 * 실제 포인트 이력이 예상된 값과 일치하는지 확인합니다.
	 * <p>
	 * 이 메서드는 예상된 포인트 이력 데이터(Map)와 실제 포인트 이력 데이터(PointHistoryResponse)를 비교합니다.
	 * 예상된 데이터의 각 필드를 실제 데이터의 필드와 비교하여 일치 여부를 확인합니다.
	 *
	 * @param expectedHistory 예상된 포인트 이력 값이 포함된 Map
	 * @param actualHistory   실제 포인트 이력 값이 포함된 PointHistoryResponse
	 * @return 모든 필드 값이 일치하면 true, 그렇지 않으면 false
	 * @throws IllegalAccessException 필드 접근에 실패한 경우 발생
	 */
	private boolean matchHistory(Map<String, String> expectedHistory, PointHistoryResponse actualHistory) throws IllegalAccessException {
		for (Map.Entry<String, String> entry : expectedHistory.entrySet()) {
			String fieldName = entry.getKey();
			String expectedValue = entry.getValue();

			Field field = getField(PointHistoryResponse.class, fieldName);
			field.setAccessible(true);
			Object actualValue = field.get(actualHistory).toString();
			if (!expectedValue.equals(actualValue)) {
				return false;
			}
		}
		return true;
	}

	@SneakyThrows
	private Field getField(Class<?> clazz, String fieldName) {
		return clazz.getDeclaredField(fieldName);
	}

	private void verifyHistoryIsEmpty() {
		PointHistoriesResponse historiesResponse = getMostRecentPointHistoriesResponse();
		assertTrue(historiesResponse.pointHistories().isEmpty(), "조회한 이력이 비어 있지 않습니다.");
	}
}