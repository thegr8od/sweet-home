package com.ssafy.house.model;

public class HouseDealResponseDto {
	private String aptSeq;
	private String aptName; // 아파트 이름
	private String floor; // 층
	private double excluUseAr; // 전용면적
	private String legalDong; // 법적동 (동 이름)
	private String tradeAmount; // 거래 금액

	// 기본 생성자
	public HouseDealResponseDto() {
	}

	public HouseDealResponseDto(String aptSeq, String aptName, String floor, double excluUseAr, String legalDong,
			String tradeAmount) {
		super();
		this.aptSeq = aptSeq;
		this.aptName = aptName;
		this.floor = floor;
		this.excluUseAr = excluUseAr;
		this.legalDong = legalDong;
		this.tradeAmount = tradeAmount;
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

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public double getExcluUseAr() {
		return excluUseAr;
	}

	public void setExcluUseAr(double excluUseAr) {
		this.excluUseAr = excluUseAr;
	}

	public String getLegalDong() {
		return legalDong;
	}

	public void setLegalDong(String legalDong) {
		this.legalDong = legalDong;
	}

	public String getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(String tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	@Override
	public String toString() {
		return "실거래가 정보 [아파트 코드=" + aptSeq + "아파트이름=" + aptName + ", 층=" + floor + ", 면적=" + excluUseAr + ", 법적동="
				+ legalDong + ", 거래금액=" + tradeAmount + "]";
	}
}
