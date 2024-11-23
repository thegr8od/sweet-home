package com.ssafy.aptboard.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ssafy.aptboard.model.AptBoardLikeDto;

@Mapper
public interface AptBoardLikeMapper {
    // 좋아요 추가
    int insertLike(AptBoardLikeDto like);
    
    // 좋아요 삭제
    int deleteLike(@Param("boardId") int boardId, @Param("userId") String userId);
    
    // 좋아요 확인
    AptBoardLikeDto getLike(@Param("boardId") int boardId, @Param("userId") String userId);
} 