package com.winston.webflux.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * check Parameters is validate
 */
@Aspect
@Slf4j
@Component
public class ParaCheckAspect {

    @Around("execution (* com.winston.webflux.handler..*(..))")
    public Object validate(ProceedingJoinPoint point) throws Throwable {
        for (int i = 0; i < point.getArgs().length; i++) {
            if (point.getArgs()[i] instanceof Mono) {
                point.getArgs()[i] = ((Mono<?>) point.getArgs()[i])
                    .doOnNext(this::check)
                    .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage())));
            } else if (point.getArgs()[i] instanceof Flux) {
                point.getArgs()[i] = ((Flux<?>) point.getArgs()[i])
                    .doOnNext(this::check)
                  .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage())));
            }
        }
        return point.proceed(point.getArgs());
    }


    private void check(Object obj) {

        System.out.println("ParaCheckAspect.check has been invoked ");
    }
}
