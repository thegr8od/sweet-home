package com.ssafy.house.model;

public class HouseDealDto {
	private int no; // 일련번호
	private String aptSeq; // 아파트 코드
	private String aptDong; // 아파트 동수
	private String floor; // 층수
	private int dealYear; // 거래 년도
	private int dealMonth; // 거래 월
	private int dealDay; // 거래 일
	private double excluUseAr; // 전용면적
	private String dealAmount; // 거래 금액

	// 기본 생성자
	public HouseDealDto() {
	}

	// 매개변수가 있는 생성자
	public HouseDealDto(int no, String aptSeq, String aptDong, String floor, int dealYear, int dealMonth, int dealDay,
			double excluUseAr, String dealAmount) {
		this.no = no;
		this.aptSeq = aptSeq;
		this.aptDong = aptDong;
		this.floor = floor;
		this.dealYear = dealYear;
		this.dealMonth = dealMonth;
		this.dealDay = dealDay;
		this.excluUseAr = excluUseAr;
		this.dealAmount = dealAmount;
	}

	// Getter 및 Setter 메서드
	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getAptSeq() {
		return aptSeq;
	}

	public void setAptSeq(String aptSeq) {
		this.aptSeq = aptSeq;
	}

	public String getAptDong() {
		return aptDong;
	}

	public void setAptDong(String aptDong) {
		this.aptDong = aptDong;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
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

	public double getExcluUseAr() {
		return excluUseAr;
	}

	public void setExcluUseAr(double excluUseAr) {
		this.excluUseAr = excluUseAr;
	}

	public String getDealAmount() {
		return dealAmount;
	}

	public void setDealAmount(String dealAmount) {
		this.dealAmount = dealAmount;
	}
}
