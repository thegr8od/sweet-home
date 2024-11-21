package com.ssafy.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.common.JwtUtil;
import com.ssafy.user.model.UserDto;
import com.ssafy.user.model.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/user")
@Tag(name = "User API", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 로그인 처리")
    public ResponseEntity<?> login(@RequestBody UserDto user) throws Exception {
        UserDto userDto = userService.searchUser(user);
        if (userDto != null) {
            String token = JwtUtil.generateToken(userDto.getId());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 잘못되었습니다.");
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "사용자 로그아웃 처리")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("로그아웃 성공");
    }

    @PostMapping("/update")
    @Operation(summary = "사용자 정보 업데이트", description = "JWT 토큰을 사용하여 사용자 정보 수정")
    public ResponseEntity<?> updateUser(@RequestBody UserDto user, @RequestHeader("Authorization") String authHeader)
            throws Exception {
        try {
            if (!authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 형식이 잘못되었습니다.");
            }

            String token = authHeader.substring(7); // "Bearer " 제거
            String userId = JwtUtil.validateToken(token);
            
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
            }

            // 토큰의 사용자 ID와 요청의 사용자 ID가 일치하는지 확인
            if (!userId.equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
            }

            userService.editUser(user);
            return ResponseEntity.ok("사용자 정보가 업데이트되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    @PostMapping("/delete")
    @Operation(summary = "사용자 삭제", description = "JWT 토큰을 사용하여 사용자 삭제")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String authHeader) throws Exception {
        try {
            if (!authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 형식이 잘못되었습니다.");
            }

            String token = authHeader.substring(7); // "Bearer " 제거
            String userId = JwtUtil.validateToken(token);
            
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
            }

            int result = userService.deleteUser(userId);
            if (result > 0) {
                return ResponseEntity.ok("사용자가 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    @PostMapping("/create")
    @Operation(summary = "회원가입", description = "새로운 사용자 생성")
    public ResponseEntity<?> createUser(@RequestBody UserDto user) throws Exception {
        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("사용자가 생성되었습니다.");
    }
    
    @GetMapping("/info")
    @Operation(summary = "사용자 정보 조회", description = "JWT 토큰을 사용하여 사용자 정보 조회")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) throws Exception {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없거나 형식이 잘못되었습니다.");
            }

            String token = authHeader.substring(7); // "Bearer " 제거
            String userId = JwtUtil.validateToken(token);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
            }

            UserDto user = userService.searchUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
            }

            // 비밀번호는 제외하고 반환
            user.setPassword(null);
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
}
