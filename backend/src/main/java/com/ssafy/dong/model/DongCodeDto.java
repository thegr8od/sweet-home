package com.ssafy.dong.model;

public class DongCodeDto {
	private String dongCode; // 동 코드
	private String sidoName; // 시도 이름
	private String gugunName; // 군구 이름
	private String dongName; // 동 이름

	// 기본 생성자
	public DongCodeDto() {
	}

	// 매개변수가 있는 생성자
	public DongCodeDto(String dongCode, String sidoName, String gugunName, String dongName) {
		this.dongCode = dongCode;
		this.sidoName = sidoName;
		this.gugunName = gugunName;
		this.dongName = dongName;
	}

	// Getter 및 Setter 메서드
	public String getDongCode() {
		return dongCode;
	}

	public void setDongCode(String dongCode) {
		this.dongCode = dongCode;
	}

	public String getSidoName() {
		return sidoName;
	}

	public void setSidoName(String sidoName) {
		this.sidoName = sidoName;
	}

	public String getGugunName() {
		return gugunName;
	}

	public void setGugunName(String gugunName) {
		this.gugunName = gugunName;
	}

	public String getDongName() {
		return dongName;
	}

	public void setDongName(String dongName) {
		this.dongName = dongName;
	}
}
