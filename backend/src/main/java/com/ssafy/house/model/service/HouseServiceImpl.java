package com.ssafy.house.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.house.mapper.HouseMapper;
import com.ssafy.house.model.HouseDealResponseDto;
import com.ssafy.house.model.HouseDealSimpleResponseDto;
import com.ssafy.house.model.HouseInfoDto;

@Service
public class HouseServiceImpl implements HouseService {

	private final HouseMapper houseMapper;

	@Autowired
	public HouseServiceImpl(HouseMapper houseMapper) {
		this.houseMapper = houseMapper;
	}

	@Override
	public List<HouseDealSimpleResponseDto> getDealsByAddress(String sido, String gugun, String dong, Integer year,
			Integer month) {
		return houseMapper.getDealsByAddress(sido, gugun, dong, year, month);
	}

	@Override
	public List<HouseDealResponseDto> getDealsByAptSeq(String aptSeq) {
		return houseMapper.getDealsByAptSeq(aptSeq);
	}

	@Override
	public List<HouseInfoDto> getHouseInfoByAptName(String aptName) {
		return houseMapper.getHouseInfoByAptName(aptName);
	}

	@Override
	public HouseInfoDto getHouseInfoByAptSeq(String aptSeq) {
		return houseMapper.getHouseInfoByAptSeq(aptSeq);
	}

	@Override
	public List<Integer> getHouseDealYearByDong(String sido, String gugun, String dong) {
		return houseMapper.getHouseDealYearByDong(sido, gugun, dong);
	}

	@Override
	public List<Integer> getHouseDealMonthByYear(String sido, String gugun, String dong, Integer year) {
		return houseMapper.getHouseDealMonthByYear(sido, gugun, dong, year);
	}

}