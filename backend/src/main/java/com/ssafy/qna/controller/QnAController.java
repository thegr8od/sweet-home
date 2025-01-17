package com.ssafy.qna.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.common.JwtUtil;
import com.ssafy.qna.model.QnADto;
import com.ssafy.qna.model.service.QnAService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/qna")
@Tag(name = "QnA API", description = "질문과 답변 관리 API")
public class QnAController {

	private final QnAService qnaService;

	public QnAController(QnAService qnaService) {
		this.qnaService = qnaService;
	}

	// QnA 목록 조회
	@GetMapping("/list")
	@Operation(summary = "QnA 목록 조회", description = "등록된 QnA의 목록을 조회합니다.")
	public ResponseEntity<List<QnADto>> listQnA() throws Exception {
		List<QnADto> qnaList = qnaService.getQnAList();
		if (qnaList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(qnaList, HttpStatus.OK);
	}

	// 질문 등록
	@PostMapping("/create")
	@Operation(summary = "질문 등록", description = "새로운 질문을 등록합니다.")
	public ResponseEntity<String> createQuestion(
	        @RequestBody QnADto qnaDto,
	        @RequestHeader("Authorization") String authHeader) throws Exception {
	    
	    try {
	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없거나 형식이 잘못되었습니다.");
	        }

	        String token = authHeader.substring(7);
	        String userid = JwtUtil.validateToken(token);

	        if (userid == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
	        }

	        // 토큰에서 추출한 사용자 ID 설정
	        qnaDto.setUserid(userid);
	        // 현재 시간 설정
	        qnaDto.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	        
	        qnaService.insertQuestion(qnaDto);
	        return ResponseEntity.status(HttpStatus.CREATED).body("질문이 등록되었습니다.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다: " + e.getMessage());
	    }
	}

	@PostMapping("/answer")
	@Operation(summary = "답변 등록", description = "질문에 대한 답변을 등록합니다. (관리자 전용)")
	public ResponseEntity<String> createAnswer(
	        @RequestBody QnADto qnaDto,
	        @RequestHeader("Authorization") String authHeader) throws Exception {
	    
	    try {
	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없거나 형식이 잘못되었습니다.");
	        }

	        String token = authHeader.substring(7);
	        String userid = JwtUtil.validateToken(token);

	        if (userid == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
	        }

	        // admin 계정 체크
	        if (!userid.equals("admin")) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자만 답변을 등록할 수 있습니다.");
	        }

	        if (qnaDto.getIdx() == 0 || qnaDto.getAnswer() == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("답변 정보를 올바르게 입력하세요.");
	        }

	        qnaService.insertAnswer(qnaDto);
	        return ResponseEntity.status(HttpStatus.CREATED).body("답변이 등록되었습니다.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
	    }
	}

	// 답변 삭제도 같은 방식으로 수정
	@DeleteMapping("/delete-answer/{idx}")
	@Operation(summary = "답변 삭제", description = "등록된 답변을 삭제합니다. (관리자 전용)")
	public ResponseEntity<String> deleteAnswer(
	        @PathVariable int idx,
	        @RequestHeader("Authorization") String authHeader) throws Exception {
	    
	    try {
	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없거나 형식이 잘못되었습니다.");
	        }

	        String token = authHeader.substring(7);
	        String userid = JwtUtil.validateToken(token);

	        if (userid == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
	        }

	        // admin 계정 체크
	        if (!userid.equals("admin")) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자만 답변을 삭제할 수 있습니다.");
	        }

	        qnaService.deleteAnswer(idx);
	        return ResponseEntity.status(HttpStatus.OK).body("답변이 삭제되었습니다.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
	    }
	}

	// 질문 수정
	@PutMapping("/update")
	@Operation(summary = "질문 수정", description = "등록된 질문을 수정합니다.")
	public ResponseEntity<String> updateQuestion(
	        @RequestBody QnADto qnaDto,
	        @RequestHeader("Authorization") String authHeader) throws Exception {
	    
	    try {
	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없거나 형식이 잘못되었습니다.");
	        }

	        String token = authHeader.substring(7);
	        String userid = JwtUtil.validateToken(token);

	        if (userid == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
	        }

	        // 작성자 본인 확인
	        if (!userid.equals(qnaDto.getUserid())) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("수정 권한이 없습니다.");
	        }

	        qnaService.updateQuestion(qnaDto);
	        return ResponseEntity.ok("질문이 수정되었습니다.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
	    }
	}
	// 질문 삭제
	@DeleteMapping("/delete/{idx}")
	@Operation(summary = "질문 삭제", description = "등록된 질문을 삭제합니다.")
	public ResponseEntity<String> deleteQuestion(
	        @PathVariable int idx,
	        @RequestHeader("Authorization") String authHeader) throws Exception {
	    
	    try {
	        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없거나 형식이 잘못되었습니다.");
	        }

	        String token = authHeader.substring(7);
	        String userid = JwtUtil.validateToken(token);

	        if (userid == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
	        }

	        // 작성자 본인 또는 관리자 확인
	        QnADto qna = qnaService.getQnA(idx); // 해당 글 정보 조회
	        if (!userid.equals(qna.getUserid()) && !userid.equals("admin")) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제 권한이 없습니다.");
	        }

	        qnaService.deleteQuestion(idx);
	        return ResponseEntity.ok("질문이 삭제되었습니다.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
	    }
	}

}
