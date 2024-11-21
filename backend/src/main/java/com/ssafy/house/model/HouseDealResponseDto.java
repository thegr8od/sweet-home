package com.ssafy.house.model;

public class HouseDealResponseDto {
	private String aptSeq;
	private String aptName; // 아파트 이름
	private String floor; // 층
	private double excluUseAr; // 전용면적
	private String legalDong; // 법적동 (동 이름)
	private String tradeAmount; // 거래 금액
	private String latitude; // 위도
	private String longitude; // 경도
	private int dealYear; // 거래 년도
	private int dealMonth; // 거래 월
	private int dealDay; // 거래 일

	// 기본 생성자
	public HouseDealResponseDto() {
	}

	// 수정된 생성자
	public HouseDealResponseDto(String aptSeq, String aptName, String floor, double excluUseAr, String legalDong,
			String tradeAmount, String latitude, String longitude, int dealYear, int dealMonth, int dealDay) {
		this.aptSeq = aptSeq;
		this.aptName = aptName;
		this.floor = floor;
		this.excluUseAr = excluUseAr;
		this.legalDong = legalDong;
		this.tradeAmount = tradeAmount;
		this.latitude = latitude;
		this.longitude = longitude;
		this.dealYear = dealYear;
		this.dealMonth = dealMonth;
		this.dealDay = dealDay;
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

	public int getDealYear() {
		return dealYear;
	}

	public void setDealYear(int dealYear) {
		this.dealYear = dealYear;
	}

	public int getDealMonth() {
		return dealMonth;
	}

	public void setDealMonth(int dealMonth) {
		this.dealMonth = dealMonth;
	}

	public int getDealDay() {
		return dealDay;
	}

	public void setDealDay(int dealDay) {
		this.dealDay = dealDay;
	}

	@Override
	public String toString() {
		return "실거래가 정보 [아파트 코드=" + aptSeq + ", 아파트이름=" + aptName + ", 층=" + floor + ", 면적=" + excluUseAr + ", 법적동="
				+ legalDong + ", 거래금액=" + tradeAmount + ", 거래일자=" + dealYear + "-" + dealMonth + "-" + dealDay + "]";
	}
}
