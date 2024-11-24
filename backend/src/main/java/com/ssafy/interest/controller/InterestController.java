package com.ssafy.interest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.interest.model.InterestDto;
import com.ssafy.interest.model.service.InterestService;
import com.ssafy.common.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/interest")
@Tag(name = "Interest API", description = "사용자의 관심 아파트 관리 API")
public class InterestController {

    private final InterestService interestService;

    public InterestController(InterestService interestService) {
        this.interestService = interestService;
    }

    /**
     * 특정 사용자의 관심 아파트 목록 조회
     */
    @GetMapping("/list")
    @Operation(summary = "관심 아파트 목록 조회", description = "로그인한 사용자의 관심 아파트 목록을 조회합니다.")
    public ResponseEntity<List<InterestDto>> getInterestsByUserId(
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String userId = JwtUtil.validateToken(token);
            
            if (userId == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            System.out.println("userId!@@@@p> "+userId);

            List<InterestDto> interests = interestService.getInterestsByUserId(userId);
            System.out.println("Retrieved interests: " + interests);  // 데이터 확인

            if (interests.isEmpty()) {
                System.out.println("No interests found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            System.out.println("Returning interests with size: " + interests.size());

            return new ResponseEntity<>(interests, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 관심 아파트 등록
     */
    @PostMapping("/{aptSeq}")
    @Operation(summary = "관심 아파트 등록", description = "사용자의 관심 아파트를 등록합니다.")
    public ResponseEntity<String> addInterest(
            @PathVariable String aptSeq,
            @RequestHeader("Authorization") String authHeader
            ) {
        try {
            String token = authHeader.substring(7);
            String userId = JwtUtil.validateToken(token);
            
            if (userId == null) {
                return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }

            System.out.println("요청 들어온 aptSeq : "+aptSeq + " userId : "+userId);
            interestService.addInterest(aptSeq, userId);
            return new ResponseEntity<>("관심 아파트가 등록되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Interest 추가 중 에러 발생: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 관심 아파트 삭제
     */
    @DeleteMapping("/{aptSeq}")
    @Operation(summary = "관심 아파트 삭제", description = "특정 관심 아파트를 삭제합니다.")
    public ResponseEntity<String> removeInterest(
            @PathVariable String aptSeq,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String userId = JwtUtil.validateToken(token);
            
            if (userId == null) {
                return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }
            
            interestService.removeInterest(aptSeq, userId);
            return new ResponseEntity<>("관심 아파트가 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
