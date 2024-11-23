package com.ssafy.aptboard.model;

import java.time.LocalDateTime;

public class AptBoardDto {
    private int id;
    private String aptSeq;
    private String userId;
    private String title;
    private String content;
    private int likesCount;
    private int commentsCount;
    private LocalDateTime date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private LocalDateTime deletedAt;
    
    // 조인에 필요한 추가 필드들
    private String userName;    // user 테이블의 name
    private String aptName;     // houseinfos 테이블의 apt_nm
    private String dongName;    // houseinfos 테이블의 umd_nm
    private boolean isLiked;    // 현재 사용자의 좋아요 여부 (추가)

    // 기본 생성자
    public AptBoardDto() {}
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

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAptName() {
        return aptName;
    }

    public void setAptName(String aptName) {
        this.aptName = aptName;
    }

    public String getDongName() {
        return dongName;
    }

    public void setDongName(String dongName) {
        this.dongName = dongName;
    }

    @Override
    public String toString() {
        return "AptBoardDto [id=" + id + ", aptSeq=" + aptSeq + ", userId=" + userId + ", title=" + title + ", content="
                + content + ", likesCount=" + likesCount + ", commentsCount=" + commentsCount + ", date=" + date + ", createdAt="
                + createdAt + ", updatedAt=" + updatedAt + ", isDeleted=" + isDeleted + ", deletedAt=" + deletedAt
                + ", userName=" + userName + ", aptName=" + aptName + ", dongName=" + dongName + ", isLiked=" + isLiked
                + "]";
    }
}
