package com.ssafy.aptboard.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.aptboard.mapper.AptBoardMapper;
import com.ssafy.aptboard.model.AptBoardDto;

@Service
public class AptBoardServiceImpl implements AptBoardService {

    private final AptBoardMapper mapper;

    public AptBoardServiceImpl(AptBoardMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void createBoard(AptBoardDto board) throws Exception {
        mapper.createBoard(board);
    }

    @Override
    public AptBoardDto getBoardById(int id) throws Exception {
        return mapper.getBoardById(id);
    }

    @Override
    public List<AptBoardDto> getBoardsByAptSeq(String aptSeq) throws Exception {
        return mapper.getBoardsByAptSeq(aptSeq);
    }

    @Override
    public void updateBoard(AptBoardDto board, String userId) throws Exception {
        // 기존 게시글 조회
        AptBoardDto existingBoard = mapper.getBoardById(board.getId());
        if (existingBoard == null || !existingBoard.getUserId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 게시글을 수정할 수 있습니다.");
        }

        // 게시글 수정
        mapper.updateBoard(board);
    }

    @Override
    public void deleteBoard(int id, String userId) throws Exception {
        // 기존 게시글 조회
        AptBoardDto existingBoard = mapper.getBoardById(id);
        if (existingBoard == null || !existingBoard.getUserId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 게시글을 삭제할 수 있습니다.");
        }

        // 게시글 삭제
        mapper.deleteBoard(id);
    }
}
