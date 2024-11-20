package com.ssafy.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    public static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    private static final String HEADER_AUTH = "Authorization"; // 토큰 헤더 필드명

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        final String token = request.getHeader(HEADER_AUTH);

        if (token != null && JwtUtil.validateToken(token) != null) {
            logger.info("유효한 토큰: {}", token);
            return true; // 토큰 유효
        } else {
            logger.warn("유효하지 않은 토큰: {}", token);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid or missing token.");
            return false;
        }
    }
}