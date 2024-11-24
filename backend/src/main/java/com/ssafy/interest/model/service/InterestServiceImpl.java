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
    public void addInterest(String aptSeq, String userId) {
        // InterestMapper.xml 파일에서
        System.out.println("Inserting values - apt_seq: " + aptSeq + ", user_id: " + userId);
        interestMapper.insertInterest(aptSeq, userId);
    }

    @Override
    public void removeInterest(String aptSeq, String userId) {
        interestMapper.deleteInterest(aptSeq, userId);
    }

    @Override
    public List<InterestDto> getInterestsByUserId(String userId) {
        return interestMapper.getInterestsByUserId(userId);
    }
}
