package com.ssafy.ssafyHome;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.house.model.HouseInfoDto;
import com.ssafy.house.model.service.HouseService;

@SpringBootTest
class SsafyHomeApplicationTests {

    @Autowired
    private HouseService houseService;

    @Test
    void getHouseInfoByAptSeqTest() {
        // 테스트할 aptSeq 값 설정 (실제 DB에 존재하는 값으로 설정)
        String testAptSeq = "29170-733";
        
        // 메서드 호출
        HouseInfoDto houseInfo = houseService.getHouseInfoByAptSeq(testAptSeq);
        
        // 결과 확인 (DB에 해당하는 aptSeq가 있는 경우에만 통과)
        assertThat(houseInfo).isNotNull();
        assertThat(houseInfo.getAptSeq()).isEqualTo(testAptSeq);
        
        // 추가 검증 (필요한 경우)
        System.out.println("Apartment Name: " + houseInfo.getAptNm());
        System.out.println("Latitude: " + houseInfo.getLatitude());
        System.out.println("Longitude: " + houseInfo.getLongitude());
    }
    
    @Test
    public void testGetHouseDealYearByDong() {
        // Given
        String sido = "서울특별시";
        String gugun = "강남구";
        String dong = "역삼동";

        // When
        List<Integer> dealYears = houseService.getHouseDealYearByDong(sido, gugun, dong);

        System.out.println(dealYears);
        
        // Then
        assertThat(dealYears).isNotNull();
        assertThat(dealYears).isNotEmpty();
        // 추가적인 검증 조건을 넣을 수 있습니다.
    }
    

    @Test
    public void testGetHouseDealMonthByYear() {
        // Given
        String sido = "서울특별시";
        String gugun = "강남구";
        String dong = "역삼동";
        Integer year = 2024; // 테스트할 년도

        // When
        List<Integer> dealMonths = houseService.getHouseDealMonthByYear(sido, gugun, dong, year);

        System.out.println(dealMonths);
        // Then
        assertThat(dealMonths).isNotNull();
        assertThat(dealMonths).isNotEmpty();
        // 추가적인 검증 조건을 넣을 수 있습니다.
        // 예: 특정한 월이 포함되어 있는지 체크
    }
}
