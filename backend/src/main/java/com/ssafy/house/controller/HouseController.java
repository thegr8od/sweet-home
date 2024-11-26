package com.ssafy.house.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.dong.model.service.DongCodeService;
import com.ssafy.house.model.HouseDealResponseDto;
import com.ssafy.house.model.HouseDealSimpleResponseDto;
import com.ssafy.house.model.HouseInfoDto;
import com.ssafy.house.model.HouseDealCoordinateDto;
import com.ssafy.house.model.service.HouseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/house")
@Tag(name = "House API", description = "아파트 실거래 및 정보 관리 API")
public class HouseController {

    private final DongCodeService dongCodeService;
    private final HouseService houseService;

    public HouseController(DongCodeService dongCodeService, HouseService houseService) {
        this.dongCodeService = dongCodeService;
        this.houseService = houseService;
    }

    @GetMapping("/deal/address")
    @Operation(summary = "아파트 실거래 정보 조회", description = "동/거래년도/월을 기준으로 아파트 실거래 정보 조회")
    public ResponseEntity<List<HouseDealSimpleResponseDto>> getHouseDealListByAddress(
            @RequestParam String sido,
            @RequestParam String gugun,
            @RequestParam String dong,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        List<HouseDealSimpleResponseDto> result = houseService.getDealsByAddress(sido, gugun, dong, year, month);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    
    @GetMapping("/deal/apt")
    @Operation(summary = "아파트 실거래 정보 조회", description = "아파트 고유번호(aptSeq)를 기준으로 실거래 정보 조회")
    public ResponseEntity<List<HouseDealResponseDto>> getHouseDealListByAptSeq(
        @RequestParam String aptSeq) {
        List<HouseDealResponseDto> result = houseService.getDealsByAptSeq(aptSeq);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    

    @GetMapping("/location")
    @Operation(summary = "지역 정보 조회", description = "시도/구군/동 정보를 조회")
    public ResponseEntity<?> getOptions(
            @RequestParam String type,
            @RequestParam(required = false) String sido,
            @RequestParam(required = false) String gugun) {
        try {
            List<String> result = null;
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
                default:
                    return new ResponseEntity<>("Invalid request type", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>("Failed to retrieve options", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{aptSeq}")
    @Operation(summary = "아파트 정보 조회", description = "아파트 코드로 상세 정보 조회")
    public ResponseEntity<?> getHouseInfoByAptSeq(@PathVariable String aptSeq) {
        HouseInfoDto houseInfo = houseService.getHouseInfoByAptSeq(aptSeq);
        if (houseInfo == null) {
            return new ResponseEntity<>("No house found with the given aptSeq", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(houseInfo, HttpStatus.OK);
    }
    
    @GetMapping("/info")
    @Operation(summary = "아파트/동 정보 조회", description = "아파트/동 이름으로 정보 조회")
    public ResponseEntity<List<HouseInfoDto>> getHouseInfoByName(
            @RequestParam String aptName) {
        System.out.println("요청된 이름: " + aptName); // 로그 추가
        List<HouseInfoDto> result = houseService.getHouseInfoByAptName(aptName);
        System.out.println("조회 결과: " + result); // 로그 추가
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @GetMapping("/dong")
    @Operation(summary = "법정동 코드 조회", description = "동 이름으로 법정동 코드 조회")
    public ResponseEntity<?> getDongInfo(@RequestParam String dongName) {
        System.out.println("=== 동 정보 조회 요청 ===");
        System.out.println("요청된 동 이름: " + dongName);
        
        HouseInfoDto result = houseService.getHouseInfoByDongName(dongName);
        System.out.println("조회 결과: " + result);
        
        if (result == null) {
            System.out.println("결과 없음");
            return new ResponseEntity<>("동 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }
        System.out.println("시군구 코드: " + result.getSggCd());
        System.out.println("읍면동 코드: " + result.getUmdCd());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/map/area")
    @Operation(summary = "좌표 범위 내 아파트 실거래 정보 조회", description = "지도의 특정 영역 내에 있는 아파트의 실거래 정보를 조회합니다")
    public ResponseEntity<List<HouseDealSimpleResponseDto>> getHouseDealsByMapBounds(
            @RequestParam String minLat,
            @RequestParam String maxLat,
            @RequestParam String minLng,
            @RequestParam String maxLng,
            @RequestParam Integer limit) {
        
        List<HouseDealSimpleResponseDto> result = houseService.getHouseDealsByCoordinates(minLat, maxLat, minLng, maxLng, limit);
        
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    

    
    @GetMapping("/map/coordinate")    
    @Operation(summary = "좌표 범위 내 아파트 좌표 정보 조회", description = "지도의 특정 영역 내에 있는 아파트의 좌표 정보를 조회합니다")
    public ResponseEntity<List<HouseDealCoordinateDto>> getHouseDealCoordinateByMapBounds(
            @RequestParam String minLat,
            @RequestParam String maxLat,
            @RequestParam String minLng,
            @RequestParam String maxLng,
            @RequestParam Integer limit) {
        
        List<HouseDealCoordinateDto> result = houseService.getHouseDealCoordinateByCoordinates(minLat, maxLat, minLng, maxLng, limit);
        
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    

}
