package com.ssafy.searchlog.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.searchlog.mapper.SearchLogMapper;

@Service
public class SearchLogServiceImpl implements SearchLogService {

    private final SearchLogMapper searchLogMapper;

    public SearchLogServiceImpl(SearchLogMapper searchLogMapper) {
        this.searchLogMapper = searchLogMapper;
    }

    @Override
    public void saveSearch(String searchKeyword) {
        searchLogMapper.saveSearchLog(searchKeyword);
    }

    @Override
    public List<Map<String, Object>> getPopularSearchKeywords(int limit) {
        return searchLogMapper.getPopularSearchKeywords(limit);
    }
}