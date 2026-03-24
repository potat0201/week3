package com.example.demo.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilter implements Filter {
    private final StringRedisTemplate redisTemplate;

    public RateLimitFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String clientIp = req.getRemoteAddr();

        String redisKey = "rate_limit:" + clientIp;

        Long requestCount = redisTemplate.opsForValue().increment(redisKey);

        if (requestCount != null && requestCount == 1) {
            redisTemplate.expire(redisKey, 60, TimeUnit.SECONDS);
        }

        if (requestCount != null && requestCount > 10) {
            res.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            res.setContentType("application/json; charset=UTF-8");
            res.getWriter().write("{\"error\": \"Bạn đã vượt quá giới hạn 10 request/phút. Vui lòng đợi!\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}