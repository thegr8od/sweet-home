package com.ssafy.dong.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.dong.mapper.DongCodeMapper;

@Service
public class DongCodeServiceImpl implements DongCodeService {

    private final DongCodeMapper dongCodeMapper;

    @Autowired
    public DongCodeServiceImpl(DongCodeMapper dongCodeMapper) {
        this.dongCodeMapper = dongCodeMapper;
    }

    @Override
    public List<String> getAllSido() throws SQLException {
        return dongCodeMapper.getAllSido();
    }

    @Override
    public List<String> getGugunBySido(String sidoCode) throws SQLException {
        return dongCodeMapper.getGugunBySido(sidoCode);
    }

    @Override
    public List<String> getDongByGugun(String sidoCode, String gugunCode) throws SQLException {
        return dongCodeMapper.getDongByGugun(sidoCode, gugunCode);
    }
}
