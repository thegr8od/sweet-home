package com.ssafy.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.board.model.BoardDto;

@Mapper
public interface BoardMapper {

    List<BoardDto> selectAll();

    BoardDto selectByCode(@Param("postId") String postId);

    int insert(BoardDto post);

    int deleteByCode(@Param("postId") String postId);

    int update(BoardDto board);

    int updateView(@Param("postId") String postId);
}
