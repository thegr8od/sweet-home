package com.ssafy.config;

import com.ssafy.common.JwtInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.*;

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
				.addPathPatterns(
					// 사용자 관련
					"/user/info", 
					"/user/update", 
					"/user/delete",
					
					// QnA 관련
					"/qna/create", 
					"/qna/answer", 
					"/qna/delete/**",
					
					// 아파트 게시판 - 인증 필요
					"/aptboard",                // POST - 게시글 생성
					"/aptboard/*/like",         // POST - 게시글 좋아요
					"/aptboard/*/unlike",       // DELETE - 게시글 좋아요 취소
					"/aptboard/comment/*",      // POST - 댓글 작성
					"/aptboard/comment/*/like", // POST - 댓글 좋아요
					"/aptboard/comment/*/unlike", // DELETE - 댓글 좋아요 취소
					"/aptboard/*",              // PUT, DELETE - 게시글 수정/삭제
					
					// 관심 아파트 관련
					"/interest/add",
					"/interest/delete/*",
					"/interest/list"
				)
				.excludePathPatterns(
					// 사용자 인증
					"/user/login", 
					"/user/create",
					
					// QnA 조회
					"/qna/list",
					
					// 아파트 게시판 - 조회 관련 (인증 불필요)
					"/aptboard/list/**",      // GET - 게시글 목록 조회
					"/aptboard/*/get",        // GET - 게시글 상세 조회
					"/aptboard/comment/list/*", // GET - 댓글 목록 조회
					
					// API 문서
					"/swagger-ui/**",     
					"/v3/api-docs/**",    
					"/error"              
				);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:5173", "http://localhost:5174", "http://localhost:5175")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.exposedHeaders("Authorization")
				.allowCredentials(true)
				.maxAge(3600);
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
