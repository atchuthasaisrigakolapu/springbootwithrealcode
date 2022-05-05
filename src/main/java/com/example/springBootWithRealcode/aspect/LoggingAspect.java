package com.example.springBootWithRealcode.aspect;

import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat.UUID;

@Aspect
@Configuration
public class LoggingAspect {

    private static final String REQUEST_ID = "RequestID";

    @Around("execution(public com.example.springBootWithRealcode.*.controller.*.*(..))")
    public Object processParams(ProceedingJoinPoint proceedingJoinPoint){

        var request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        var response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        var requestId = Optional.ofNullable(request.getHeader("RequestID"))
                .orElse(Optional.ofNullable(RequestContextHolder.getRequestAttributes().getAttribute("RequestID", RequestAttributes.SCOPE_REQUEST))
                        .map(Object::toString)
                        .orElse(java.util.UUID.randomUUID().toString()));
        MDC.put("RequestID",requestId);
        MDC.put("TransactionURL",request.getMethod()+" "+request.getRequestURI());
        var result = proceed(proceedingJoinPoint);
        MDC.clear();
        if(Objects.nonNull(response))
            response.setHeader("REQUEST-ID",requestId);

        return result;
    }

    @SneakyThrows
    private Object proceed(ProceedingJoinPoint proceedingJoinPoint) {
        return  proceedingJoinPoint.proceed();
    }

}
