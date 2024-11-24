package com.ssafy.interest.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor  // 기본 생성자
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자
@ToString
public class InterestDto {
    private String userId;
    private String aptSeq;
    private String aptName;    // 추가
	private String legalDong; // 추가
    private String latitude;   // 추가
    private String longitude;  // 추가
}