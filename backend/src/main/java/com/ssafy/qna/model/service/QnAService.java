package com.ssafy.qna.model.service;

import java.util.List;

import com.ssafy.qna.model.QnADto;

public interface QnAService {
    List<QnADto> getQnAList() throws Exception; // QnA 목록 조회

    int insertQuestion(QnADto qnaDto) throws Exception; // 질문 등록

    int insertAnswer(QnADto qnaDto) throws Exception; // 답변 등록

    int updateQuestion(QnADto qnaDto) throws Exception; // 질문 수정

    int deleteQuestion(int idx) throws Exception; // 질문 삭제

    int deleteAnswer(int idx) throws Exception; // 답변 삭제
    
    QnADto getQnA(int idx) throws Exception;
    
    
}