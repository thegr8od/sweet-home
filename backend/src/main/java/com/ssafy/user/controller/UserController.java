package com.ssafy.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ssafy.user.model.UserDto;
import com.ssafy.user.model.service.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	
	@PostMapping("login")
	public String login(UserDto user, HttpSession session, RedirectAttributes redirectAttributes) throws Exception {
		UserDto userDto = userService.searchUser(user);
		System.out.println("결과는?"+userDto);
		if(userDto != null) { // 로그인 성공 
			session.setAttribute("userinfo", userDto);
			System.out.println("성공!");
			return "redirect:/";
		}else {
			System.out.println("실패");
			redirectAttributes.addFlashAttribute("loginError", "아이디 또는 비밀번호가 잘못되었습니다.");
			return "redirect:/";
		}
		
	}
	
	@PostMapping("/logout")
	private String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@PostMapping("/update")
	private String updateUser(UserDto user , HttpSession session) throws Exception {
		userService.editUser(user);
		session.setAttribute("userinfo", user);
		return "redirect:/";
	}
	
	@PostMapping("/delete")
	private String deleteUser(UserDto user,HttpSession session) throws Exception {
		UserDto dto = (UserDto) session.getAttribute("userinfo");
		userService.deleteUser(dto.getId());
		session.invalidate();
		return "redirect:/";
	}
	
	@PostMapping("/create")
	private String createUser(UserDto user) throws Exception {
		userService.registerUser(user);
		return "redirect:/";
	}


}
