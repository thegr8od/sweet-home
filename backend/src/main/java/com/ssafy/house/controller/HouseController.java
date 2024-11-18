package com.ssafy.house.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.ssafy.dong.model.service.DongCodeService;
import com.ssafy.house.model.HouseDealResponseDto;
import com.ssafy.house.model.HouseInfoDto;
import com.ssafy.house.model.service.HouseService;

@RestController
@RequestMapping("/house")
public class HouseController {

	private final DongCodeService dongCodeService;
	private final HouseService houseService;

	@Autowired
	public HouseController(DongCodeService dongCodeService, HouseService houseService) {
		this.dongCodeService = dongCodeService;
		this.houseService = houseService;
	}

	// 동별+거래년도+월별 아파트 실거래 정보 조회
	@GetMapping("/houseDealList")
	public String getHouseDealList(
			@RequestParam String sido, 
			@RequestParam String gugun, 
			@RequestParam String dong,
			@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer month) 
	{
		List<HouseDealResponseDto> result = houseService.getDealsByDong(sido, gugun, dong, year, month);
		return new Gson().toJson(result);
	}

	// 시도, 구군, 동 정보 조회
	@GetMapping("/location")
	public String getOptions(@RequestParam String type, @RequestParam(required = false) String sido,
			@RequestParam(required = false) String gugun) {

		List<String> result = null;
		try {
			switch (type) {
			case "sido":
				result = dongCodeService.getAllSido();
				break;
			case "gugun":
				if (sido != null) {
					result = dongCodeService.getGugunBySido(sido);
				}
				break;
			case "dong":
				if (sido != null && gugun != null) {
					result = dongCodeService.getDongByGugun(sido, gugun);
				}
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "{\"error\": \"Failed to retrieve options\"}";
		}
		return new Gson().toJson(result);
	}

	// 시도, 구군, 동으로 거래 년도 조회
	@GetMapping("/dealYears")
	public String getDealYearsByDong(@RequestParam String sido, @RequestParam String gugun, @RequestParam String dong) {
		List<Integer> dealYears = houseService.getHouseDealYearByDong(sido, gugun, dong);
		return new Gson().toJson(dealYears);
	}

	// 거래 년도로 월 조회
	@GetMapping("/dealMonths")
	public String getDealMonthsByYear(@RequestParam String sido, @RequestParam String gugun, @RequestParam String dong,
			@RequestParam Integer year) {
		List<Integer> dealMonths = houseService.getHouseDealMonthByYear(sido, gugun, dong, year);
		return new Gson().toJson(dealMonths);
	}

	// 아파트 코드로 아파트 정보 조회
	@GetMapping("/{aptSeq}")
	public String getHouseInfoByAptSeq(@PathVariable String aptSeq) {
		System.out.println("아파트 코드 " + aptSeq);
		HouseInfoDto houseInfo = houseService.getHouseInfoByAptSeq(aptSeq);
		System.out.println("아파트 info " + houseInfo);
		if (houseInfo == null) {
			return "{\"error\": \"No house found with the given aptSeq\"}";
		}
		return new Gson().toJson(houseInfo);
	}

}
