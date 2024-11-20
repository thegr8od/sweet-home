package com.ssafy.house.model.service;

import java.util.List;

import com.ssafy.house.model.HouseDealResponseDto;
import com.ssafy.house.model.HouseDealSimpleResponseDto;
import com.ssafy.house.model.HouseInfoDto;


public interface HouseService {

	// 구미시 진평동의 아파트 실거래 정보 조회
	List<HouseDealSimpleResponseDto> getDealsByAddress(String sido, String gugun, String dong, Integer year, Integer month);

	// 특정 아파트 이름으로 실거래 정보 조회
	List<HouseDealResponseDto> getDealsByAptName(String aptName);
	
	// 아파트 코드로 아파트 정보 조회
	HouseInfoDto getHouseInfoByAptSeq(String aptSeq);
	
	// 동별 거래 년도 조회
	List<Integer> getHouseDealYearByDong(String sido, String gugun, String dong);
	
	// 거래년도 별 월 조회
	List<Integer> getHouseDealMonthByYear(String sido, String gugun, String dong, Integer month);

}
