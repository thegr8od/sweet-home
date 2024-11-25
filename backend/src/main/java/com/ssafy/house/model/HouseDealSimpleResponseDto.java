package com.ssafy.house.model;

public class HouseDealSimpleResponseDto {
	private String aptSeq;
	private String aptName;
	private String legalDong;
	private String latitude;
	private String longitude;
	private String maxPrice;
	private String maxPriceArea;

	// 기본 생성자
	public HouseDealSimpleResponseDto() {
	}

	// 모든 필드를 포함한 생성자
	public HouseDealSimpleResponseDto(String aptSeq, String aptName, String legalDong, String latitude,
			String longitude, String maxPrice, String maxPriceArea) {
		super();
		this.aptSeq = aptSeq;
		this.aptName = aptName;
		this.legalDong = legalDong;
		this.latitude = latitude;
		this.longitude = longitude;
		this.maxPrice = maxPrice;
		this.maxPriceArea = maxPriceArea;
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

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getMaxPriceArea() {
		return maxPriceArea;
	}

	public void setMaxPriceArea(String maxPriceArea) {
		this.maxPriceArea = maxPriceArea;
	}

	@Override
	public String toString() {
		return "HouseDealSimpleResponseDto [aptSeq=" + aptSeq + ", aptName=" + aptName + ", legalDong=" + legalDong
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", maxPrice=" + maxPrice 
				+ ", maxPriceArea=" + maxPriceArea + "]";
	}


}
