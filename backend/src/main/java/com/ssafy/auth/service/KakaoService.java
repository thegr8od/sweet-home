package com.ssafy.auth.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ssafy.auth.model.KakaoUserInfo;
import com.ssafy.user.model.UserDto;
import com.ssafy.user.model.service.UserService;
import com.ssafy.common.JwtUtil;

@Service
public class KakaoService {
    
    @Value("${kakao.client-id}")
    private String clientId;
    
    @Value("${kakao.redirect-uri}")
    private String redirectUri;
    
    private final RestTemplate restTemplate;
    private final UserService userService;
    
    // 생성자 주입
    public KakaoService(RestTemplate restTemplate, UserService userService) {
        this.restTemplate = restTemplate;
        this.userService = userService;
    }
    
    public String processKakaoLogin(String code) throws Exception {
        // 1. 인가코드로 액세스 토큰 받기
        String accessToken = getAccessToken(code);
        
        // 2. 액세스 토큰으로 사용자 정보 받기
        KakaoUserInfo userInfo = getKakaoUserInfo(accessToken);
        
        // 3. 카카오 ID로 회원가입 여부 확인
        String kakaoId = "KAKAO_" + userInfo.getId();
        UserDto user = userService.searchUserById(kakaoId);
        
        // 4. 회원가입이 안되어있다면 회원가입 처리
        if (user == null) {
            UserDto newUser = new UserDto();
            newUser.setId(kakaoId);
            newUser.setName(userInfo.getKakaoAccount().getProfile().getNickname());
            newUser.setProfileImage(userInfo.getKakaoAccount().getProfile().getProfileImageUrl());
            newUser.setPassword("KAKAO_LOGIN");
            newUser.setEmail(kakaoId + "@kakao.user");
            
            userService.registerUser(newUser);
            user = newUser;
        }
        
        // 5. JWT 토큰 생성 및 반환
        return JwtUtil.generateToken(user.getId());
    }
    
    @SuppressWarnings("unchecked")
    private String getAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        
        System.out.println("Token request params: " + params); // 디버깅용
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            System.out.println("Token response: " + response.getBody()); // 디버깅용
            return (String) response.getBody().get("access_token");
        } catch (Exception e) {
            System.err.println("Error getting access token: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    private KakaoUserInfo getKakaoUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        HttpEntity<String> request = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                request,
                KakaoUserInfo.class
            );
            
            // 디버깅을 위한 로그 추가
            System.out.println("Kakao user info response: " + response.getBody());
            KakaoUserInfo userInfo = response.getBody();
            System.out.println("Profile image URL: " + 
                userInfo.getKakaoAccount().getProfile().getProfileImageUrl());
            
            return userInfo;
        } catch (Exception e) {
            System.err.println("Error getting user info: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}