package io.hhplus.tdd.point.api.infrastructure.persistence.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.hhplus.tdd.point.api.domain.entity.UserPoint;
import io.hhplus.tdd.point.api.infrastructure.database.UserPointTable;

/**
 * @author : Rene Choi
 * @since : 2024/06/19
 */

@ExtendWith(MockitoExtension.class)
public class SimpleUserPointRepositoryTest {

	@Mock
	private UserPointTable userPointTable;

	@InjectMocks
	private SimpleUserPointRepository userPointRepository;

	private UserPoint sampleUserPoint;

	@BeforeEach
	void setUp() {
		sampleUserPoint = new UserPoint(1L, 100L, System.currentTimeMillis());
	}

	@Test
	@DisplayName("특정 ID로 UserPoint를 조회할 때, 올바르게 반환되는지 테스트")
	void testSelectById() {
		// Given
		when(userPointTable.selectById(anyLong())).thenReturn(sampleUserPoint);

		// When
		UserPoint result = userPointRepository.selectById(1L);

		// Then
		assertEquals(sampleUserPoint, result);
	}

	@Test
	@DisplayName("UserPoint를 삽입 또는 업데이트할 때, 올바르게 반환되는지 테스트")
	void testInsertOrUpdate() {
		// Given
		when(userPointTable.insertOrUpdate(anyLong(), anyLong())).thenReturn(sampleUserPoint);

		// When
		UserPoint result = userPointRepository.insertOrUpdate(1L, 100L);

		// Then
		assertEquals(sampleUserPoint, result);
	}
}
