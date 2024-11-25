package com.ssafy.interest.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class InterestDto {
    private String userId;
    private String aptSeq;
    private String aptName;    // 추가
	private String legalDong; // 추가
    private String latitude;   // 추가
    private String longitude;  // 추가
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
	public String getAptName() {
		return aptName;
	}
	public void setAptName(String aptName) {
		this.aptName = aptName;
	}
	public String getLegalDong() {
		return legalDong;
	}
	public void setLegalDong(String legalDong) {
		this.legalDong = legalDong;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public InterestDto() {
		// TODO Auto-generated constructor stub
	}
	
	public InterestDto(String userId, String aptSeq, String aptName, String legalDong, String latitude,
			String longitude) {
		super();
		this.userId = userId;
		this.aptSeq = aptSeq;
		this.aptName = aptName;
		this.legalDong = legalDong;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "InterestDto [userId=" + userId + ", aptSeq=" + aptSeq + ", aptName=" + aptName + ", legalDong="
				+ legalDong + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
    

    
}