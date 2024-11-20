package com.ssafy.interest.model;

public class InterestDto {

	private String userId;
	private String aptSeq;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAptSeq() {
		return aptSeq;
	}
	public void setAptSeq(String aptSeq) {
		this.aptSeq = aptSeq;
	}
	public InterestDto(String userId, String aptSeq) {
		super();
		this.userId = userId;
		this.aptSeq = aptSeq;
	}
	@Override
	public String toString() {
		return "InterestDto [userId=" + userId + ", aptSeq=" + aptSeq + "]";
	}
	
	
	
}
