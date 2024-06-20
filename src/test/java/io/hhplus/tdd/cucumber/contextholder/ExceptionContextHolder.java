package io.hhplus.tdd.cucumber.contextholder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
public class ExceptionContextHolder {

	private static final ConcurrentHashMap<String, Exception> exceptionMap = new ConcurrentHashMap<>();
	private static final AtomicReference<Exception> lastException = new AtomicReference<>();

	public static void initFields() {
		exceptionMap.clear();
		lastException.set(null);
	}

	public static void putException(Exception exception) {
		String key = String.valueOf(System.currentTimeMillis());
		exceptionMap.put(key, exception);
		lastException.set(exception);
	}

	public static Exception getException(String key) {
		return exceptionMap.get(key);
	}

	public static Exception getMostRecentException() {
		return lastException.get();
	}
}