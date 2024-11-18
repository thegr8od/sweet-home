package com.ssafy.house.model;

public class HouseInfoDto {
	private String aptSeq; // 아파트 코드
	private String sggCd; // 시구군 코드
	private String umdCd; // 읍면동 코드
	private String umdNm; // 읍면동 이름
	private String jibun; // 지번
	private String roadNmSggCd; // 도로명 시구군 코드
	private String roadNm; // 도로명
	private String roadNmBonbun; // 도로명 본번
	private String roadNmBubun; // 도로명 부번
	private String aptNm; // 아파트 이름
	private int buildYear; // 건축년도
	private String latitude; // 위도
	private String longitude; // 경도

	// 기본 생성자
	public HouseInfoDto() {
	}

	// 매개변수가 있는 생성자
	public HouseInfoDto(String aptSeq, String sggCd, String umdCd, String umdNm, String jibun, String roadNmSggCd,
			String roadNm, String roadNmBonbun, String roadNmBubun, String aptNm, int buildYear, String latitude,
			String longitude) {
		this.aptSeq = aptSeq;
		this.sggCd = sggCd;
		this.umdCd = umdCd;
		this.umdNm = umdNm;
		this.jibun = jibun;
		this.roadNmSggCd = roadNmSggCd;
		this.roadNm = roadNm;
		this.roadNmBonbun = roadNmBonbun;
		this.roadNmBubun = roadNmBubun;
		this.aptNm = aptNm;
		this.buildYear = buildYear;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	// Getter 및 Setter 메서드
	public String getAptSeq() {
		return aptSeq;
	}

	public void setAptSeq(String aptSeq) {
		this.aptSeq = aptSeq;
	}

	public String getSggCd() {
		return sggCd;
	}

	public void setSggCd(String sggCd) {
		this.sggCd = sggCd;
	}

	public String getUmdCd() {
		return umdCd;
	}

	public void setUmdCd(String umdCd) {
		this.umdCd = umdCd;
	}

	public String getUmdNm() {
		return umdNm;
	}

	public void setUmdNm(String umdNm) {
		this.umdNm = umdNm;
	}

	public String getJibun() {
		return jibun;
	}

	public void setJibun(String jibun) {
		this.jibun = jibun;
	}

	public String getRoadNmSggCd() {
		return roadNmSggCd;
	}

	public void setRoadNmSggCd(String roadNmSggCd) {
		this.roadNmSggCd = roadNmSggCd;
	}

	public String getRoadNm() {
		return roadNm;
	}

	public void setRoadNm(String roadNm) {
		this.roadNm = roadNm;
	}

	public String getRoadNmBonbun() {
		return roadNmBonbun;
	}

	public void setRoadNmBonbun(String roadNmBonbun) {
		this.roadNmBonbun = roadNmBonbun;
	}

	public String getRoadNmBubun() {
		return roadNmBubun;
	}

	public void setRoadNmBubun(String roadNmBubun) {
		this.roadNmBubun = roadNmBubun;
	}

	public String getAptNm() {
		return aptNm;
	}

	public void setAptNm(String aptNm) {
		this.aptNm = aptNm;
	}

	public int getBuildYear() {
		return buildYear;
	}

	public void setBuildYear(int buildYear) {
		this.buildYear = buildYear;
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
}
