package io.hhplus.tdd.point.common.distributedlock;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import io.hhplus.tdd.point.common.annotation.DistributedLock;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
@Aspect
@Component
public class DistributedLockAspect {

	private final DistributedLockManager lockManager;

	@Autowired
	public DistributedLockAspect(DistributedLockManager lockManager) {
		this.lockManager = lockManager;
	}

	@Around("@annotation(lockAnnotation)")
	public Object around(ProceedingJoinPoint joinPoint, DistributedLock lockAnnotation) throws Throwable {
		String key = generateKey(joinPoint, lockAnnotation);
		lockManager.lock(key);
		try {
			return joinPoint.proceed();
		} finally {
			lockManager.unlock(key);
		}
	}

	private String generateKey(ProceedingJoinPoint joinPoint, DistributedLock lockAnnotation) {
		String prefix = lockAnnotation.prefix();
		String[] keys = lockAnnotation.keys();
		String keyString = Arrays.stream(keys)
			.map(key -> getValueFromArgs(joinPoint, key))
			.collect(Collectors.joining(":"));
		return prefix + ":" + keyString;
	}

	private String getValueFromArgs(ProceedingJoinPoint joinPoint, String key) {
		Object[] args = joinPoint.getArgs();
		String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		for (int i = 0; i < paramNames.length; i++) {
			context.setVariable(paramNames[i], args[i]);
		}
		return parser.parseExpression("#" + key).getValue(context, String.class);
	}
}

