package com.ssafy.interest.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.interest.mapper.InterestMapper;
import com.ssafy.interest.model.InterestDto;

@Service
public class InterestServiceImpl implements InterestService {

    private final InterestMapper interestMapper;

    public InterestServiceImpl(InterestMapper interestMapper) {
        this.interestMapper = interestMapper;
    }

    @Override
    public void addInterest(InterestDto interestDto) throws Exception {
        // 관심 아파트 등록
        interestMapper.insertInterest(interestDto);
    }

    @Override
    public void removeInterest(int no) throws Exception {
        // 관심 아파트 삭제
        int deletedCount = interestMapper.deleteInterest(no);
        if (deletedCount == 0) {
            throw new Exception("삭제할 항목이 존재하지 않습니다.");
        }
    }

    @Override
    public List<InterestDto> getInterestsByUserId(String userId) throws Exception {
        // 특정 사용자의 관심 아파트 목록 조회
        List<InterestDto> interests = interestMapper.getInterestsByUserId(userId);
        if (interests.isEmpty()) {
            throw new Exception("관심 아파트 목록이 존재하지 않습니다.");
        }
        return interests;
    }
}
