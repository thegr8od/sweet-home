package com.ssafy.searchlog.model.service;

import java.util.List;
import java.util.Map;

public interface SearchLogService {
    void saveSearch(String searchKeyword); // 검색 로그 저장
    List<Map<String, Object>> getPopularSearchKeywords(int limit); // 인기 검색어 조회
}