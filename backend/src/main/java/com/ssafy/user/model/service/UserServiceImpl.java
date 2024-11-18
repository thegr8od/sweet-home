package com.ssafy.user.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.user.mapper.UserMapper;
import com.ssafy.user.model.UserDto;

@Service
public class UserServiceImpl implements UserService {

	private final UserMapper mapper;

	public UserServiceImpl(UserMapper mapper) throws Exception {
		super();
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
		System.out.println(userDto.getId());
		System.out.println(userDto.getPassword());
		return mapper.searchUser(userDto);
	}

	@Override
	public void editUser(UserDto userDto) throws Exception {
		mapper.editUser(userDto);
	}

	@Override
	public int deleteUser(String userId) throws Exception {
		return mapper.deleteUser(userId);
	}

}
