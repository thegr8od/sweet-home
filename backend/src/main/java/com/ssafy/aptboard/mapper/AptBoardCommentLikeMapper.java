package com.ssafy.aptboard.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ssafy.aptboard.model.AptBoardCommentLikeDto;

@Mapper
public interface AptBoardCommentLikeMapper {
    // 댓글 좋아요 추가
    int insertCommentLike(AptBoardCommentLikeDto like);
    
    // 댓글 좋아요 삭제
    int deleteCommentLike(@Param("commentId") int commentId, @Param("userId") String userId);
    
    // 댓글 좋아요 확인
    AptBoardCommentLikeDto getCommentLike(@Param("commentId") int commentId, @Param("userId") String userId);
} 