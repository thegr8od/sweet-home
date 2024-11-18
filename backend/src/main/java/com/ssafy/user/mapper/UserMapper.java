package com.ssafy.user.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.user.model.UserDto;

@Mapper
public interface UserMapper {

	int registerUser(UserDto userDto) throws SQLException;
	List<UserDto> searchAllUser() throws SQLException;
	UserDto searchUser(UserDto userDto) throws SQLException;
	void editUser(UserDto userDto) throws SQLException;
	int deleteUser(String userId) throws SQLException;
}