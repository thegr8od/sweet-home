package com.ssafy.dong.model.service;

import java.sql.SQLException;
import java.util.List;

public interface DongCodeService {
	public List<String> getAllSido() throws SQLException;

	public List<String> getGugunBySido(String sidoCode) throws SQLException;

	public List<String> getDongByGugun(String sidoCode, String gugunCode) throws SQLException;

}
