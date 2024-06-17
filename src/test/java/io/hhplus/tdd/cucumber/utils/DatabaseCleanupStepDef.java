package io.hhplus.tdd.cucumber.utils;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java8.En;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
public class DatabaseCleanupStepDef implements En {

	@Autowired
	private DatabaseCleanupExecutor databaseCleanupExecutor;

	public DatabaseCleanupStepDef() {
		Before(() -> databaseCleanupExecutor.execute());
	}
}
