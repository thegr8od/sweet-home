package com.ssafy.auth.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ssafy.auth.service.KakaoService;
import com.ssafy.auth.model.KakaoLoginRequest;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    
    private final KakaoService kakaoService;
    
    // 생성자 주입
    public AuthController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }
    
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoCallback(@RequestBody KakaoLoginRequest request) {
        try {
            System.out.println("Received code: " + request.getCode()); // 디버깅용
            String token = kakaoService.processKakaoLogin(request.getCode());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // 스택 트레이스 출력
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}