package com.ssafy.aptboard.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ssafy.aptboard.model.AptBoardCommentDto;

@Mapper
public interface AptBoardCommentMapper {
    // 댓글 생성
    int createComment(AptBoardCommentDto comment);
    
    // 게시글의 모든 댓글 조회 (userId는 선택적)
    List<AptBoardCommentDto> getCommentsByBoardId(@Param("boardId") int boardId, @Param("userId") String userId);
    
    // 특정 댓글 조회 (userId는 선택적)
    AptBoardCommentDto getCommentById(@Param("id") int id, @Param("userId") String userId);
    
    // 댓글 수정
    int updateComment(AptBoardCommentDto comment);
    
    // 댓글 소프트 삭제
    int deleteComment(int id);
} 