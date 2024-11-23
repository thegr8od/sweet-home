package com.ssafy.aptboard.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.aptboard.mapper.AptBoardMapper;
import com.ssafy.aptboard.mapper.AptBoardLikeMapper;
import com.ssafy.aptboard.model.AptBoardDto;
import com.ssafy.aptboard.model.AptBoardLikeDto;

@Service
public class AptBoardServiceImpl implements AptBoardService {

    private final AptBoardMapper boardMapper;
    private final AptBoardLikeMapper likeMapper;

    public AptBoardServiceImpl(AptBoardMapper boardMapper, AptBoardLikeMapper likeMapper) {
        this.boardMapper = boardMapper;
        this.likeMapper = likeMapper;
    }

    @Override
    public void createBoard(AptBoardDto board) throws Exception {
        boardMapper.createBoard(board);
    }

    @Override
    public AptBoardDto getBoardById(int id, String userId) throws Exception {
        return boardMapper.getBoardById(id, userId);
    }

    @Override
    public List<AptBoardDto> getBoardsByAptSeq(String aptSeq, String userId) throws Exception {
        return boardMapper.getBoardsByAptSeq(aptSeq, userId);
    }

    @Override
    public void updateBoard(AptBoardDto board, String userId) throws Exception {
        AptBoardDto existingBoard = boardMapper.getBoardById(board.getId(), userId);
        if (existingBoard == null || !existingBoard.getUserId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 게시글을 수정할 수 있습니다.");
        }
        boardMapper.updateBoard(board);
    }

    @Override
    public void deleteBoard(int id, String userId) throws Exception {
        AptBoardDto existingBoard = boardMapper.getBoardById(id, userId);
        if (existingBoard == null || !existingBoard.getUserId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 게시글을 삭제할 수 있습니다.");
        }
        boardMapper.deleteBoard(id);
    }

    @Override
    @Transactional
    public void likeBoard(int boardId, String userId) throws Exception {
        AptBoardDto board = boardMapper.getBoardById(boardId, userId);
        if (board == null || board.isDeleted()) {
            throw new IllegalStateException("존재하지 않는 게시글입니다.");
        }
        
        if (likeMapper.getLike(boardId, userId) != null) {
            throw new IllegalStateException("이미 좋아요를 누른 게시글입니다.");
        }
        
        AptBoardLikeDto like = new AptBoardLikeDto();
        like.setBoardId(boardId);
        like.setUserId(userId);
        likeMapper.insertLike(like);
    }

    @Override
    @Transactional
    public void unlikeBoard(int boardId, String userId) throws Exception {
        AptBoardDto board = boardMapper.getBoardById(boardId, userId);
        if (board == null || board.isDeleted()) {
            throw new IllegalStateException("존재하지 않는 게시글입니다.");
        }
        
        if (likeMapper.getLike(boardId, userId) == null) {
            throw new IllegalStateException("좋아요를 누르지 않은 게시글입니다.");
        }
        
        likeMapper.deleteLike(boardId, userId);
    }
}
