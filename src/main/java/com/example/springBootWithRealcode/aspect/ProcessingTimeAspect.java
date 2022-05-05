package com.example.springBootWithRealcode.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@Aspect
@Slf4j
public class ProcessingTimeAspect {

    public Object monitorProcessingTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String message = getMessage(proceedingJoinPoint);
        long start = System.currentTimeMillis();
        var future = proceedingJoinPoint.proceed();
        if(future instanceof Mono){
            Mono<Object> mono = (Mono<Object>) future;
            return mono.doOnNext(o -> displayProcessingTime(message, start))
                    .doOnError(o->displayProcessingTime(message, start));
        }
        else{
            displayProcessingTime(message, start);
            return future;
        }
    }
    private void displayProcessingTime(String message,long startTime){
        long timeTaken = System.currentTimeMillis() - startTime;
        log.info("Processing Time :: {} :: {}", message, timeTaken);

    }
    private String getMessage(ProceedingJoinPoint proceedingJoinPoint){
        var signature = (MethodSignature) proceedingJoinPoint.getSignature();
        MonitoringProcesingTime annotation = signature.getMethod().getAnnotation(MonitoringProcesingTime.class);
        String msg = "";
        if(Objects.nonNull(annotation) && (!StringUtils.isEmpty(annotation.operation()) && (!StringUtils.isEmpty(annotation.serviceType())))){
            msg = annotation.serviceType() + "::" + annotation.operation();
        }
        else{
            String methodName = signature.getMethod().getName();
            Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
            msg=constructMethodName(methodName,parameterTypes);
        }
        return msg;
    }

    private String constructMethodName(String methodName, Class<?>[] parameterTypes) {

        return methodName + "(" +String.join(",", Arrays.stream(parameterTypes).map(Class::getSimpleName).collect(Collectors.toList()))+ ")";
    }


}
