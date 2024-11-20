package com.ssafy.searchlog.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SearchLogMapper {

    // 검색 로그 저장
    void saveSearchLog(@Param("searchKeyword") String searchKeyword);

    // 인기 검색어 조회
    List<Map<String, Object>> getPopularSearchKeywords(@Param("limit") int limit);
}
