package com.ssafy.board.model.service;

import java.util.List;

import com.ssafy.board.model.BoardDto;

public interface BoardService {
	List<BoardDto> selectAll() throws Exception;

	BoardDto selectByCode(String code) throws Exception;

	int insert(BoardDto product) throws Exception;

	int deleteByCode(String code) throws Exception;

	int update(BoardDto product) throws Exception;

	int updateView(String postId) throws Exception;
}
