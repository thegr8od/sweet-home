package com.ssafy.house.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.house.model.HouseDealResponseDto;
import com.ssafy.house.model.HouseDealSimpleResponseDto;
import com.ssafy.house.model.HouseInfoDto;

@Mapper
public interface HouseMapper {

	// 동별 아파트 실거래 정보 조회
	List<HouseDealSimpleResponseDto> getDealsByAddress(
			@Param("sido") String sido, @Param("gugun") String gugun,
			@Param("dong") String dong, @Param("year") Integer year, @Param("month") Integer month);

	// 특정 아파트 이름으로 실거래 정보 조회
	List<HouseDealResponseDto> getDealsByAptName(@Param("aptName") String aptName);

	// aptSeq로 아파트 정보 조회
	HouseInfoDto getHouseInfoByAptSeq(@Param("aptSeq") String aptSeq);

	// 동별 거래 년도 조회
	List<Integer> getHouseDealYearByDong(
			@Param("sido") String sido, @Param("gugun") String gugun,
			@Param("dong") String dong);
	
	// 거래 년도 별 월 조회
	List<Integer> getHouseDealMonthByYear(
			@Param("sido") String sido, @Param("gugun") String gugun,
			@Param("dong") String dong, @Param("year") Integer year);
}
