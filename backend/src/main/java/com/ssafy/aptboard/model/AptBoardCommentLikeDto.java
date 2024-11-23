package com.ssafy.aptboard.model;

import java.time.LocalDateTime;

public class AptBoardCommentLikeDto {
    private int id;
    private int commentId;
    private String userId;
    private LocalDateTime createdAt;

    public AptBoardCommentLikeDto() {}

    // getter/setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 