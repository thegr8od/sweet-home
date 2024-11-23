package com.ssafy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.*;

import com.ssafy.common.JwtInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    @Value("${upload.dir}")
    private String uploadDir;

    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/user/info", "/user/update", "/user/delete", "/qna/create", "/qna/answer", "/qna/delete/**")
                .excludePathPatterns("/user/login", "/user/create", "/qna/list");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .maxAge(3600); // 1시간 동안 preflight 결과를 캐시
    }

    // 프로필 이미지 접근을 위한 리소스 핸들러 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = "file:///C:/uploads/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath)
                .setCachePeriod(0);  // 캐시 비활성화
        
        System.out.println("Resource path configured: " + uploadPath); // 로그 추가
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
