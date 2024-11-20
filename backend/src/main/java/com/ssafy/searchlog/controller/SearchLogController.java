package com.ssafy.searchlog.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.searchlog.model.service.SearchLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/searchlog")
@Tag(name = "Search Log API", description = "검색 로그 관리 API")
public class SearchLogController {

    private final SearchLogService searchLogService;

    public SearchLogController(SearchLogService searchLogService) {
        this.searchLogService = searchLogService;
    }

    @PostMapping
    @Operation(summary = "검색 로그 저장", description = "검색 키워드를 기반으로 로그 저장")
    public ResponseEntity<Void> saveSearchLog(@RequestParam String searchKeyword) {
        searchLogService.saveSearch(searchKeyword);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/popular")
    @Operation(summary = "인기 검색어 조회", description = "인기 검색어 상위 목록 조회")
    public ResponseEntity<List<Map<String, Object>>> getPopularSearchKeywords(@RequestParam(defaultValue = "10") int limit) {
        List<Map<String, Object>> popularKeywords = searchLogService.getPopularSearchKeywords(limit);
        return new ResponseEntity<>(popularKeywords, HttpStatus.OK);
    }
}
