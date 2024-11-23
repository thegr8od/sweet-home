package com.ssafy.user.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.user.model.UserDto;

@Mapper
public interface UserMapper {

    int registerUser(UserDto userDto) throws SQLException;

    List<UserDto> searchAllUser() throws SQLException;

    UserDto searchUser(UserDto userDto) throws SQLException;

    void editUser(UserDto userDto) throws SQLException;

    UserDto searchUserById(String userId) throws SQLException;

    int deleteUser(String userId) throws SQLException;

    int changePassword(@Param("userId") String userId, @Param("currentPassword") String currentPassword,
                       @Param("newPassword") String newPassword) throws SQLException;

    void updateProfileImage(@Param("userId") String userId, @Param("profileImage") String profileImage) throws SQLException;
}
