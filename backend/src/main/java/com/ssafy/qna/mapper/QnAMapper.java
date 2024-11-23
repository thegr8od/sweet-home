package com.ssafy.qna.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.qna.model.QnADto;

@Mapper
public interface QnAMapper {
    List<QnADto> getQnAList(); // QnA 목록 조회

    int insertQuestion(QnADto qnaDto); // 질문 등록

    int insertAnswer(QnADto qnaDto); // 답변 등록

    int updateQuestion(QnADto qnaDto); // 질문 수정

    int deleteQuestion(int idx); // 질문 삭제

    int deleteAnswer(int idx); // 답변 삭제
    
    QnADto getQnA(int idx) throws Exception;
}
