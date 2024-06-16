package io.hhplus.tdd.point.common.transaction;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            userPointTableProxy.beginTransaction();
            result = pjp.proceed();
            userPointTableProxy.commitTransaction();
        } catch (Exception e) {
            userPointTableProxy.rollbackTransaction();
            throw e;
        }
        return result;
    }
}
