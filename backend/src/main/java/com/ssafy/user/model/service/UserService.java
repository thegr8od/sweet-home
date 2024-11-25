package com.ssafy.user.model.service;

import java.util.List;

import com.ssafy.user.model.UserDto;

public interface UserService {

    int registerUser(UserDto userDto) throws Exception;

    List<UserDto> searchAllUser() throws Exception;

    void editUser(UserDto userDto) throws Exception;

    UserDto searchUser(UserDto userDto) throws Exception;

    int deleteUser(String userId) throws Exception;

    UserDto searchUserById(String userId) throws Exception;

    boolean changePassword(String userId, String currentPassword, String newPassword) throws Exception;

    void updateProfileImage(String userId, String profileImage) throws Exception;

    boolean resetPassword(String email) throws Exception;
}
