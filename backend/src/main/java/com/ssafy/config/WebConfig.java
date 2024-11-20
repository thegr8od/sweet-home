package com.ssafy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.common.JwtInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 기본적으로 모든 경로는 인증이 필요하지 않음
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns(
                        // 인증이 필요한 경로만 지정
                        "/user/update", "/user/delete",          // 사용자 정보 수정 및 삭제
                        "/interest/add", "/interest/delete/**", // 관심 아파트 추가 및 삭제
                        "/aptboard", "/aptboard/{id}",          // 게시글 작성 및 수정
                        "/aptboard/delete/**",                  // 게시글 삭제
                        "/qna/create", "/qna/answer",           // 질문, 답변 등록
                        "/qna/update",                          // 질문 수정
                        "/qna/delete/**", "/qna/delete-answer/**" // 질문/답변 삭제
                );
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 URL 허용
                .allowedOrigins("*") // 모든 도메인 허용
                .allowedMethods("*") // 모든 HTTP 메서드 허용 (GET, POST, PUT, DELETE 등)
                .allowedHeaders("*") // 모든 헤더 허용
                .allowCredentials(false); // 쿠키는 허용하지 않음
    }
}
