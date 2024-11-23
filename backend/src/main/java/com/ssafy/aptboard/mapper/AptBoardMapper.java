package com.ssafy.aptboard.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ssafy.aptboard.model.AptBoardDto;

@Mapper
public interface AptBoardMapper {
    // 게시글 생성
    int createBoard(AptBoardDto board);
    
    // 게시글 상세 조회 (userId는 선택적)
    AptBoardDto getBoardById(@Param("id") int id, @Param("userId") String userId);
    
    // 아파트별 게시글 목록 조회 (userId는 선택적)
    List<AptBoardDto> getBoardsByAptSeq(@Param("aptSeq") String aptSeq, @Param("userId") String userId);
    
    // 게시글 수정
    int updateBoard(AptBoardDto board);
    
    // 게시글 소프트 삭제
    int deleteBoard(int id);
}
