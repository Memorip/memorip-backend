package com.example.memorip.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("within(com.example.memorip.controller.*)")
    public void controller() {}

    @Before("controller()")
    public void beforeRequest(JoinPoint joinPoint) {
        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);
        log.info("🟨🟨🟨🟨🟨🟨🟨🟨 Start Request 🟨🟨🟨🟨🟨🟨🟨🟨", method.getName());
        log.info("메서드 이름: {}", method.getName());

        // 파라미터 받아오기
        Object[] args = joinPoint.getArgs();
        if (args.length <= 0) log.info("파라미터가 없습니다.");
        for (Object arg : args) {
            log.info("{} 타입의 {} 파라미터가 있습니다.", arg.getClass().getSimpleName(), arg);
        }
    }

    @AfterReturning(pointcut = "controller()", returning = "returnValue")
    public void afterReturningLogging(JoinPoint joinPoint, Object returnValue) {
        log.info("🟩🟩🟩🟩🟩🟩🟩🟩 End request 🟩🟩🟩🟩🟩🟩🟩🟩");
        log.info(joinPoint.getSignature().toShortString());

        if (returnValue == null) return;

        log.info("{} 컨트롤러에서 {}를 반환했습니다.", joinPoint.getSignature().toShortString(), returnValue.toString());

    }

    @AfterThrowing(pointcut =  "controller()", throwing = "e")
            public void afterThrowingLogging(JoinPoint joinPoint, Exception e) {
        log.error("🟥🟥🟥🟥🟥🟥🟥🟥 Occured error in request {} 🟥🟥🟥🟥🟥🟥🟥🟥", joinPoint.getSignature().toShortString());
        log.error("\t{}", e.getMessage());
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }


}

