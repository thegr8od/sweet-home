package com.ssafy.interest.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.interest.model.InterestDto;
import com.ssafy.interest.model.service.InterestService;

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
    @GetMapping("/list/{userId}")
    @Operation(summary = "관심 아파트 목록 조회", description = "특정 사용자의 관심 아파트 목록을 조회합니다.")
    public ResponseEntity<List<InterestDto>> getInterestsByUserId(@PathVariable String userId) {
        try {
            List<InterestDto> interests = interestService.getInterestsByUserId(userId);
            if (interests.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(interests, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 관심 아파트 등록
     */
    @PostMapping("/add")
    @Operation(summary = "관심 아파트 등록", description = "사용자의 관심 아파트를 등록합니다.")
    public ResponseEntity<String> addInterest(@RequestBody InterestDto interestDto) {
        try {
            interestService.addInterest(interestDto);
            return new ResponseEntity<>("관심 아파트가 등록되었습니다.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 관심 아파트 삭제
     */
    @DeleteMapping("/delete/{no}")
    @Operation(summary = "관심 아파트 삭제", description = "특정 관심 아파트를 삭제합니다.")
    public ResponseEntity<String> removeInterest(@PathVariable int no) {
        try {
            interestService.removeInterest(no);
            return new ResponseEntity<>("관심 아파트가 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
