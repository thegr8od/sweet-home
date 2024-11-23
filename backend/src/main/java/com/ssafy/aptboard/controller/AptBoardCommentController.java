package com.ssafy.aptboard.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ssafy.aptboard.model.AptBoardCommentDto;
import com.ssafy.aptboard.model.service.AptBoardCommentService;
import com.ssafy.common.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/aptboard/comment")
@Tag(name = "Apartment Board Comment API", description = "아파트 게시판 댓글 관리 API")
public class AptBoardCommentController {

    private final AptBoardCommentService commentService;

    public AptBoardCommentController(AptBoardCommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{boardId}")
    @Operation(summary = "댓글 작성", description = "게시글에 새로운 댓글 작성")
    public ResponseEntity<String> createComment(
            @PathVariable int boardId,
            @RequestBody AptBoardCommentDto comment,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String userId = JwtUtil.validateToken(token);
            
            if (userId == null) {
                return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }
            comment.setBoardId(boardId);
            comment.setUserId(userId);
            commentService.createComment(comment);
            return new ResponseEntity<>("댓글이 작성되었습니다.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("댓글 작성 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list/{boardId}")
    @Operation(summary = "댓글 목록 조회", description = "게시글의 모든 댓글 조회")
    public ResponseEntity<List<AptBoardCommentDto>> getComments(
            @PathVariable int boardId,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String userId = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                userId = JwtUtil.validateToken(token);
            }

            List<AptBoardCommentDto> comments = commentService.getCommentsByBoardId(boardId, userId);
            if (comments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글 내용 수정")
    public ResponseEntity<String> updateComment(
            @PathVariable int commentId,
            @RequestBody AptBoardCommentDto comment,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String userId = JwtUtil.validateToken(token);
            
            if (userId == null) {
                return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }
            comment.setId(commentId);
            commentService.updateComment(comment, userId);
            return new ResponseEntity<>("댓글이 수정되었습니다.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("댓글 수정 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글 삭제")
    public ResponseEntity<String> deleteComment(
            @PathVariable int commentId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String userId = JwtUtil.validateToken(token);
            
            if (userId == null) {
                return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }
            commentService.deleteComment(commentId, userId);
            return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("댓글 삭제 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{commentId}/like")
    @Operation(summary = "댓글 좋아요", description = "댓글에 좋아요 추가")
    public ResponseEntity<String> likeComment(
            @PathVariable int commentId,
            @RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String tokenUserId = JwtUtil.validateToken(token);
            String requestUserId = request.get("userId");
            
            if (tokenUserId == null || !tokenUserId.equals(requestUserId)) {
                return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }
            
            commentService.likeComment(commentId, requestUserId);
            return new ResponseEntity<>("좋아요가 추가되었습니다.", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("좋아요 추가 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{commentId}/like")
    @Operation(summary = "댓글 좋아요 취소", description = "댓글 좋아요 취소")
    public ResponseEntity<String> unlikeComment(
            @PathVariable int commentId,
            @RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String tokenUserId = JwtUtil.validateToken(token);
            String requestUserId = request.get("userId");
            
            if (tokenUserId == null || !tokenUserId.equals(requestUserId)) {
                return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }
            
            commentService.unlikeComment(commentId, requestUserId);
            return new ResponseEntity<>("좋아요가 취소되었습니다.", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("좋아요 취소 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
} 