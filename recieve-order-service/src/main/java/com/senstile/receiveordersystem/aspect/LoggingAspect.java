package com.senstile.receiveordersystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Value("${logging.callLimit}")
    private int callLimit;

    @Around("@annotation(LogExecutionTime)")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        String methodName = methodSignature.getName();
        Object[] array = proceedingJoinPoint.getArgs();

        final StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        if (stopWatch.getTotalTimeSeconds() > callLimit) {
            log.warn("Database call of {} for args {} took {} seconds", methodName, array, stopWatch.getTotalTimeSeconds());
        }

        return result;
    }
}
