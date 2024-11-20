package com.ssafy.aptboard.model;

import java.time.LocalDateTime;

public class AptBoardDto {
    private int id;
    private String aptSeq;
    private String userId;
    private String title;
    private String content;
    private LocalDateTime date;

    public AptBoardDto() {}

    public AptBoardDto(int id, String aptSeq, String userId, String title, String content, LocalDateTime date) {
        this.id = id;
        this.aptSeq = aptSeq;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAptSeq() {
        return aptSeq;
    }

    public void setAptSeq(String aptSeq) {
        this.aptSeq = aptSeq;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
