package com.ssafy.board.model;

public class BoardDto {
	private int postId;
	private String userId;
	private String title;
	private String content;
	private int view;
	private String createdAt;

	public BoardDto() {

	}

	public BoardDto(int postId, String userId, String title, String content, int view, String createdAt) {
		this.postId = postId;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.view = view;
		this.createdAt = createdAt;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
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

	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "BoardDto [postId=" + postId + ", userId=" + userId + ", title=" + title + ", content=" + content
				+ ", createdAt=" + createdAt + "]";
	}

}
