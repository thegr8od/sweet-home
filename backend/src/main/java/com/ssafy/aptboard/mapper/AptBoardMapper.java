package com.ssafy.aptboard.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.aptboard.model.AptBoardDto;

@Mapper
public interface AptBoardMapper {
    int createBoard(AptBoardDto board);
    AptBoardDto getBoardById(int id);
    List<AptBoardDto> getBoardsByAptSeq(String aptSeq);
    int updateBoard(AptBoardDto board);
    int deleteBoard(int id);
}
