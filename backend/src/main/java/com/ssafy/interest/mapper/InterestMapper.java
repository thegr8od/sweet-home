package com.ssafy.interest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.interest.model.InterestDto;

@Mapper
public interface InterestMapper {
    int insertInterest(String aptSeq, String userId);  // 관심 아파트 등록
    int deleteInterest(String aptSeq, String userId);  // 관심 아파트 삭제
    List<InterestDto> getInterestsByUserId(String userId);  // 사용자의 관심 아파트 목록 조회
}