package com.ssafy.aptboard.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.aptboard.model.AptBoardDto;
import com.ssafy.aptboard.model.service.AptBoardService;
import com.ssafy.common.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/aptboard")
@Tag(name = "Apartment Board API", description = "아파트 게시판 관리 API")
public class AptBoardController {

    private final AptBoardService boardService;

    public AptBoardController(AptBoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    @Operation(summary = "게시글 생성", description = "새로운 게시글 생성")
    public ResponseEntity<String> createBoard(@RequestBody AptBoardDto board) {
        try {
            boardService.createBoard(board);
            return new ResponseEntity<>("게시글이 생성되었습니다.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("게시글 생성 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시글 조회", description = "ID를 기반으로 게시글 상세 조회")
    public ResponseEntity<AptBoardDto> getBoardById(@PathVariable int id) {
        try {
            return new ResponseEntity<>(boardService.getBoardById(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list/{aptSeq}")
    @Operation(summary = "아파트 게시글 목록 조회", description = "아파트 코드를 기반으로 게시글 목록 조회")
    public ResponseEntity<List<AptBoardDto>> getBoardsByAptSeq(@PathVariable String aptSeq) {
        try {
            return new ResponseEntity<>(boardService.getBoardsByAptSeq(aptSeq), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정", description = "JWT 토큰을 사용하여 게시글 수정")
    public ResponseEntity<String> updateBoard(
            @PathVariable int id,
            @RequestBody AptBoardDto board,
            @RequestHeader("Authorization") String token) {
        try {
            String userId = JwtUtil.validateToken(token);
            if (userId == null) {
                return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }
            board.setId(id);
            boardService.updateBoard(board, userId);
            return new ResponseEntity<>("게시글이 수정되었습니다.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("게시글 수정 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제", description = "JWT 토큰을 사용하여 게시글 삭제")
    public ResponseEntity<String> deleteBoard(
            @PathVariable int id,
            @RequestHeader("Authorization") String token) {
        try {
            String userId = JwtUtil.validateToken(token);
            if (userId == null) {
                return new ResponseEntity<>("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED);
            }
            boardService.deleteBoard(id, userId);
            return new ResponseEntity<>("게시글이 삭제되었습니다.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("게시글 삭제 실패: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
