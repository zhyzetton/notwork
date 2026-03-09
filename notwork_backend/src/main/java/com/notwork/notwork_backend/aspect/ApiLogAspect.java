package com.notwork.notwork_backend.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLogAspect {

    private final ObjectMapper objectMapper;

    @Around("execution(* com.notwork.notwork_backend.controller..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURI();
        String method = request.getMethod();
        String ip = request.getRemoteAddr();
        String args;
        try {
            args = objectMapper.writeValueAsString(joinPoint.getArgs());
        } catch (Exception e) {
            args = "无法序列化参数";
        }

        log.info("==> 请求: {} {} | IP: {} | 参数: {}", method, url, ip, args);

        long start = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;
            log.info("<== 响应: {} {} | 耗时: {}ms | 状态: 成功", method, url, elapsed);
        } catch (Throwable e) {
            long elapsed = System.currentTimeMillis() - start;
            log.error("<== 响应: {} {} | 耗时: {}ms | 异常: {}", method, url, elapsed, e.getMessage());
            throw e;
        }

        return result;
    }
}
