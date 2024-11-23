package com.ssafy.aptboard.model.service;

import java.util.List;

import com.ssafy.aptboard.model.AptBoardCommentLikeDto;
import org.springframework.stereotype.Service;
import com.ssafy.aptboard.mapper.AptBoardCommentMapper;
import com.ssafy.aptboard.mapper.AptBoardCommentLikeMapper;
import com.ssafy.aptboard.model.AptBoardCommentDto;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AptBoardCommentServiceImpl implements AptBoardCommentService {

    private final AptBoardCommentMapper commentMapper;
    private final AptBoardCommentLikeMapper commentLikeMapper;

    public AptBoardCommentServiceImpl(AptBoardCommentMapper commentMapper, AptBoardCommentLikeMapper commentLikeMapper) {
        this.commentMapper = commentMapper;
        this.commentLikeMapper = commentLikeMapper;
    }

    @Override
    public void createComment(AptBoardCommentDto comment) throws Exception {
        commentMapper.createComment(comment);
    }

    @Override
    public List<AptBoardCommentDto> getCommentsByBoardId(int boardId, String userId) throws Exception {
        return commentMapper.getCommentsByBoardId(boardId, userId);
    }

    @Override
    public void updateComment(AptBoardCommentDto comment, String userId) throws Exception {
        AptBoardCommentDto existingComment = commentMapper.getCommentById(comment.getId(), userId);
        if (existingComment == null || !existingComment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 댓글을 수정할 수 있습니다.");
        }
        commentMapper.updateComment(comment);
    }

    @Override
    public void deleteComment(int commentId, String userId) throws Exception {
        AptBoardCommentDto existingComment = commentMapper.getCommentById(commentId, userId);
        if (existingComment == null || !existingComment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 댓글을 삭제할 수 있습니다.");
        }
        commentMapper.deleteComment(commentId);
    }

    @Override
    @Transactional
    public void likeComment(int commentId, String userId) throws Exception {
        AptBoardCommentDto comment = commentMapper.getCommentById(commentId, userId);
        if (comment == null || comment.isDeleted()) {
            throw new IllegalStateException("존재하지 않는 댓글입니다.");
        }
        
        if (commentLikeMapper.getCommentLike(commentId, userId) != null) {
            throw new IllegalStateException("이미 좋아요를 누른 댓글입니다.");
        }
        
        AptBoardCommentLikeDto like = new AptBoardCommentLikeDto();
        like.setCommentId(commentId);
        like.setUserId(userId);
        commentLikeMapper.insertCommentLike(like);
    }

    @Override
    @Transactional
    public void unlikeComment(int commentId, String userId) throws Exception {
        AptBoardCommentDto comment = commentMapper.getCommentById(commentId, userId);
        if (comment == null || comment.isDeleted()) {
            throw new IllegalStateException("존재하지 않는 댓글입니다.");
        }
        
        if (commentLikeMapper.getCommentLike(commentId, userId) == null) {
            throw new IllegalStateException("좋아요를 누르지 않은 댓글입니다.");
        }
        
        commentLikeMapper.deleteCommentLike(commentId, userId);
    }
} 