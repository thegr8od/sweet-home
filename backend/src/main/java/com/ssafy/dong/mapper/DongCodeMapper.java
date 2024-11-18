package com.ssafy.dong.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DongCodeMapper {
    List<String> getAllSido();

    List<String> getGugunBySido(@Param("sidoName") String sidoName);

    List<String> getDongByGugun(@Param("sidoName") String sidoName, @Param("gugunName") String gugunName);
}
