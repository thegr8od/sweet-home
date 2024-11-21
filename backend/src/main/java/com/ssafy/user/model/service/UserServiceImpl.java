package com.ssafy.user.model.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.user.mapper.UserMapper;
import com.ssafy.user.model.UserDto;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;

    public UserServiceImpl(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public int registerUser(UserDto userDto) throws Exception {
        return mapper.registerUser(userDto);
    }

    @Override
    public List<UserDto> searchAllUser() throws Exception {
        return mapper.searchAllUser();
    }

    @Override
    public UserDto searchUser(UserDto userDto) throws Exception {
        return mapper.searchUser(userDto);
    }

    @Override
    public void editUser(UserDto userDto) throws Exception {
        mapper.editUser(userDto);
    }

    @Override
    @Transactional
    public int deleteUser(String userId) throws Exception {
        // 실제 삭제가 아닌 소프트 삭제 수행
        return mapper.deleteUser(userId);
    }
    
    @Override
    public UserDto searchUserById(String userId) throws Exception {
        return mapper.searchUserById(userId);
    }
    
 
}
