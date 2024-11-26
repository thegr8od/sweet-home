package com.ssafy.house.model;

public class HouseDealCoordinateDto {
	private String aptSeq;
	private String aptName;
	private String legalDong;
	private String latitude;
	private String longitude;

	// 기본 생성자
	public HouseDealCoordinateDto() {
	}

	// 모든 필드를 포함한 생성자
	public HouseDealCoordinateDto(String aptSeq, String aptName, String legalDong, String latitude,
			String longitude) {
		super();
		this.aptSeq = aptSeq;
		this.aptName = aptName;
		this.legalDong = legalDong;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getAptSeq() {
		return aptSeq;
	}

	public void setAptSeq(String aptSeq) {
		this.aptSeq = aptSeq;
	}

	// Getter 및 Setter 메서드
	public String getAptName() {
		return aptName;
	}

	public void setAptName(String aptName) {
		this.aptName = aptName;
	}

	public String getLegalDong() {
		return legalDong;
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

	public void setLegalDong(String legalDong) {
		this.legalDong = legalDong;
	}

	@Override
	public String toString() {
		return "HouseDealSimpleResponseDto [aptSeq=" + aptSeq + ", aptName=" + aptName + ", legalDong=" + legalDong
				+ ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}


}
