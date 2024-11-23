package com.ssafy.aptboard.model.service;

import java.util.List;
import com.ssafy.aptboard.model.AptBoardCommentDto;

public interface AptBoardCommentService {
    void createComment(AptBoardCommentDto comment) throws Exception;
    
    List<AptBoardCommentDto> getCommentsByBoardId(int boardId, String userId) throws Exception;
    
    void updateComment(AptBoardCommentDto comment, String userId) throws Exception;
    
    void deleteComment(int commentId, String userId) throws Exception;

    void likeComment(int commentId, String userId) throws Exception;

    void unlikeComment(int commentId, String userId) throws Exception;
} 