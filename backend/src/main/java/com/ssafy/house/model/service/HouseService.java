package com.ssafy.house.model.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ssafy.house.model.HouseDealResponseDto;
import com.ssafy.house.model.HouseDealSimpleResponseDto;
import com.ssafy.house.model.HouseDealCoordinateDto;
import com.ssafy.house.model.HouseInfoDto;

public interface HouseService {

	// 주소로 아파트 실거래 정보 조회
	List<HouseDealSimpleResponseDto> getDealsByAddress(String sido, String gugun, String dong, Integer year,
			Integer month);

	// 아파트 번호로 실거래 정보 조회
	List<HouseDealResponseDto> getDealsByAptSeq(String aptSeq);

	// 특정 아파트 이름으로 아파트 정보 조회
	List<HouseInfoDto> getHouseInfoByAptName(String aptName);

	// 아파트 코드로 아파트 정보 조회
	HouseInfoDto getHouseInfoByAptSeq(String aptSeq);

	// 동별 거래 년도 조회
	List<Integer> getHouseDealYearByDong(String sido, String gugun, String dong);

	// 거래년도 별 월 조회
	List<Integer> getHouseDealMonthByYear(String sido, String gugun, String dong, Integer month);

	// 인터페이스에 메서드 추가
	HouseInfoDto getDongCodeByName(String dongName);
	
	HouseInfoDto getHouseInfoByDongName(String dongName);

	// 특정 좌표 범위 내의 아파트 정보 조회 메서드 추가
	List<HouseInfoDto> getHouseInfosByCoordinates(String minLat, String maxLat, String minLng, String maxLng);

	// 특정 좌표 범위 내의 아파트 실거래 정보 조회
	List<HouseDealSimpleResponseDto> getHouseDealsByCoordinates(String minLat, String maxLat, String minLng, String maxLng, Integer limit);

	// 특정 좌표 범위 내의 아파트 좌표 정보 조회
	List<HouseDealCoordinateDto> getHouseDealCoordinateByCoordinates(String minLat, String maxLat, String minLng, String maxLng, Integer limit);
}
