package com.ssafy.user.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${upload.dir}")
    private String uploadDir;

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
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없거나 형식이 잘못되었습니다.");
            }

            String token = authHeader.substring(7);
            String userId = JwtUtil.validateToken(token);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
            }

            UserDto userDto = userService.searchUserById(userId);
            if (userDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
            }


            if (userDto.getProfileImage() != null && !userDto.getProfileImage().isEmpty()) {
                // 전체 URL 경로 설정
                userDto.setProfileImageUrl("/user/profile-image/" + userId);
                System.out.println("Profile Image URL set to: " + userDto.getProfileImageUrl()); // 디버깅용
            }

            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    @PostMapping("/change-password")
    @Operation(summary = "비밀번호 변경", description = "현재 비밀번호 확인 후 새로운 비밀번호로 변경")
    public ResponseEntity<?> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> passwordData) {
        try {
            if (!authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 형식이 잘못되었습니다.");
            }

            String token = authHeader.substring(7);
            String userId = JwtUtil.validateToken(token);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
            }

            String currentPassword = passwordData.get("currentPassword");
            String newPassword = passwordData.get("newPassword");
            String confirmPassword = passwordData.get("confirmPassword");

            if (!newPassword.equals(confirmPassword)) {
                return ResponseEntity.badRequest().body("새 비밀번호가 일치하지 않습니다.");
            }

            boolean success = userService.changePassword(userId, currentPassword, newPassword);

            if (success) {
                return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("현재 비밀번호가 일치하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    @PostMapping("/upload-profile")
    @Operation(summary = "프로필 사진 업로드", description = "사용자의 프로필 사진을 업로드합니다.")
    public ResponseEntity<?> uploadProfileImage(@RequestParam("profileImage") MultipartFile file,
                                                @RequestHeader("Authorization") String authHeader) {
        try {
            if (!authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰 형식이 잘못되었습니다.");
            }

            String token = authHeader.substring(7);
            String userId = JwtUtil.validateToken(token);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
            }

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 비어있습니다.");
            }

            // 파일명 생성 및 저장
            String filename = StringUtils.cleanPath(userId + "_" + file.getOriginalFilename());
            File dest = new File(uploadDir + filename);

            // 업로드 디렉토리가 없으면 생성
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            // 파일 저장
            file.transferTo(dest);

            // 데이터베이스에 파일 경로 저장
            userService.updateProfileImage(userId, filename);

            return ResponseEntity.ok("프로필 사진이 업로드되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 중 오류가 발생했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    @GetMapping("/profile-image/{userId}")
    @Operation(summary = "프로필 사진 조회", description = "사용자의 프로필 사진을 반환합니다.")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable String userId) {
        try {
            UserDto user = userService.searchUserById(userId);
            if (user == null || user.getProfileImage() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            File imgFile = new File(uploadDir + user.getProfileImage());
            if (!imgFile.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            byte[] imageBytes = java.nio.file.Files.readAllBytes(imgFile.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "image/jpeg"); // 이미지 타입에 맞게 변경
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
