package com.ssafy.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.board.mapper.BoardMapper;
import com.ssafy.board.model.BoardDto;

@Service
public class BoardServiceImpl implements BoardService {

	private final BoardMapper boardMapper;

	@Autowired
	public BoardServiceImpl(BoardMapper boardMapper) {
		this.boardMapper = boardMapper;
	}

	@Override
	public List<BoardDto> selectAll() throws Exception {
		return boardMapper.selectAll();
	}

	@Override
	public BoardDto selectByCode(String code) throws Exception {
		return boardMapper.selectByCode(code);
	}

	@Override
	public int insert(BoardDto product) throws Exception {
		return boardMapper.insert(product);
	}

	@Override
	public int deleteByCode(String code) throws Exception {
		return boardMapper.deleteByCode(code);
	}

	@Override
	public int update(BoardDto product) throws Exception {
		return boardMapper.update(product);
	}

	@Override
	public int updateView(String postId) throws Exception {
		return boardMapper.updateView(postId);
	}
}
