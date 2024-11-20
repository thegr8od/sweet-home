package com.ssafy.searchlog.model;

import java.time.LocalDateTime;

public class SearchLogDto {
    private int id; // primary key
    private String searchKeyword; // 검색 키워드
    private int searchCount; // 검색 횟수
    private LocalDateTime createdAt; // 생성 날짜

    // 기본 생성자
    public SearchLogDto() {}

    // 매개변수 있는 생성자
    public SearchLogDto(int id, String searchKeyword, int searchCount, LocalDateTime createdAt) {
        this.id = id;
        this.searchKeyword = searchKeyword;
        this.searchCount = searchCount;
        this.createdAt = createdAt;
    }

    // Getter 및 Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "SearchLogDto{" +
                "id=" + id +
                ", searchKeyword='" + searchKeyword + '\'' +
                ", searchCount=" + searchCount +
                ", createdAt=" + createdAt +
                '}';
    }
}
