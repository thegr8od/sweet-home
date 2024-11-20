package com.ssafy.aptboard.model.service;

import java.util.List;

import com.ssafy.aptboard.model.AptBoardDto;

public interface AptBoardService {

    void createBoard(AptBoardDto board) throws Exception;

    AptBoardDto getBoardById(int id) throws Exception;

    List<AptBoardDto> getBoardsByAptSeq(String aptSeq) throws Exception;

    void updateBoard(AptBoardDto board, String userId) throws Exception; // 수정자 검증 추가

    void deleteBoard(int id, String userId) throws Exception; // 삭제자 검증 추가
}
