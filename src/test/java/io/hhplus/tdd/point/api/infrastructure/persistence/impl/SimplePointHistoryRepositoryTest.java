package io.hhplus.tdd.point.api.infrastructure.persistence.impl;

import static io.hhplus.tdd.point.common.model.types.TransactionType.*;
import static java.lang.System.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.hhplus.tdd.point.api.domain.entity.PointHistory;
import io.hhplus.tdd.point.api.infrastructure.database.PointHistoryTable;

/**
 * @author : Rene Choi
 * @since : 2024/06/19
 */

@ExtendWith(MockitoExtension.class)
public class SimplePointHistoryRepositoryTest {

	@Mock
	private PointHistoryTable pointHistoryTable;

	@InjectMocks
	private SimplePointHistoryRepository pointHistoryRepository;

	private PointHistory samplePointHistory;

	@BeforeEach
	void setUp() {
		samplePointHistory = new PointHistory(1L, 1L, 100L, CHARGE, currentTimeMillis());
	}

	@Test
	@DisplayName("PointHistory를 삽입할 때, 올바르게 반환되는지 테스트")
	void testInsert() {
		// Given
		when(pointHistoryTable.insert(anyLong(), anyLong(), eq(CHARGE), anyLong())).thenReturn(samplePointHistory);

		// When
		PointHistory result = pointHistoryRepository.insert(1L, 100L, CHARGE, currentTimeMillis());

		// Then
		assertEquals(samplePointHistory, result);
	}

	@Test
	@DisplayName("특정 유저 ID로 모든 PointHistory를 조회할 때, 올바르게 반환되는지 테스트")
	void testSelectAllByUserId() {
		// Given
		List<PointHistory> historyList = new ArrayList<>();
		historyList.add(samplePointHistory);
		when(pointHistoryTable.selectAllByUserId(anyLong())).thenReturn(historyList);

		// When
		List<PointHistory> result = pointHistoryRepository.selectAllByUserId(1L);

		// Then
		assertEquals(historyList, result);
	}
}
