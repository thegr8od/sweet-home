package com.ssafy.interest.model.service;

import java.util.List;

import com.ssafy.interest.model.InterestDto;

public interface InterestService {

    // 관심 아파트 등록
    void addInterest(InterestDto interestDto) throws Exception;

    // 관심 아파트 삭제
    void removeInterest(int no) throws Exception;

    // 특정 사용자의 관심 아파트 목록 조회
    List<InterestDto> getInterestsByUserId(String userId) throws Exception;
}