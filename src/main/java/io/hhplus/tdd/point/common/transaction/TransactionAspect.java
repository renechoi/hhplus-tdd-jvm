package io.hhplus.tdd.point.common.transaction;

import java.lang.reflect.Field;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@Aspect
@Component
@Slf4j
public class TransactionAspect {

	private final UserPointTableProxy userPointTableProxy;

	@Autowired
	public TransactionAspect(UserPointTableProxy userPointTableProxy) {
		this.userPointTableProxy = userPointTableProxy;
	}

	@Around("@annotation(io.hhplus.tdd.point.common.annotation.Transactional)")
	public Object manageTransaction(ProceedingJoinPoint pjp) throws Throwable {
		Object result;
		try {
			log.info("begin transaction");
			Object[] args = pjp.getArgs();
			for (Object arg : args) {
				Long userId = extractUserId(arg);
				if (userId != null) {
					userPointTableProxy.addUserId(userId);
				}
			}
			userPointTableProxy.beginTransaction();
			result = pjp.proceed();
			userPointTableProxy.commitTransaction();
			log.info("commit transaction");
		} catch (Exception e) {
			log.info("rollback transaction");
			userPointTableProxy.rollbackTransaction();
			throw e;
		}
		return result;
	}

	@SneakyThrows
	private Long extractUserId(Object arg) {
		if (arg instanceof Long) {
			return (Long)arg;
		}
		Field idField = arg.getClass().getDeclaredField("id");
		idField.setAccessible(true);
		return (Long)idField.get(arg);
	}
}
