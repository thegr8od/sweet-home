package com.ssafy.aptboard.model.service;

import java.util.List;

import com.ssafy.aptboard.model.AptBoardDto;

public interface AptBoardService {

    void createBoard(AptBoardDto board) throws Exception;

    AptBoardDto getBoardById(int id, String userId) throws Exception;

    List<AptBoardDto> getBoardsByAptSeq(String aptSeq, String userId) throws Exception;

    void updateBoard(AptBoardDto board, String userId) throws Exception;

    void deleteBoard(int id, String userId) throws Exception;

    void likeBoard(int boardId, String userId) throws Exception;

    void unlikeBoard(int boardId, String userId) throws Exception;
}
