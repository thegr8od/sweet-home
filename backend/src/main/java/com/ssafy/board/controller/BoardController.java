package com.ssafy.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssafy.board.model.BoardDto;
import com.ssafy.board.model.service.BoardService;
import com.ssafy.user.model.UserDto;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/board")
public class BoardController {

	private final BoardService boardService;

	@Autowired
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

	@GetMapping("/list")
	public String list(Model model) {
		try {
			System.out.println("공지사항 리스트 조회 시작");
			List<BoardDto> list = boardService.selectAll();
			model.addAttribute("list", list);
			System.out.println(list);
			System.out.println("공지사항 리스트 조회 완료");
			return "board/list"; // 뷰 이름 리턴 (src/main/resources/templates/board/list.html)
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	@GetMapping("/view")
	public ModelAndView view(@RequestParam("postId") String postId) {
		ModelAndView mav = new ModelAndView("board/view");
		try {
			BoardDto post = boardService.selectByCode(postId);
			mav.addObject("post", post);
			boardService.updateView(postId);
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("error");
		}
		return mav;
	}

	@PostMapping("/write")
	public String write(@ModelAttribute BoardDto boardDto, HttpSession session) {
		UserDto user = (UserDto) session.getAttribute("userinfo");
		if (user != null) {
			boardDto.setUserId(user.getId());
			try {
				boardService.insert(boardDto);
				return "redirect:/board/list";
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
		} else {
			System.out.println("로그인 필요");
			return "redirect:/board/list";
		}
	}

	@PostMapping("/update")
	public String update(@ModelAttribute BoardDto boardDto) {
		try {
			boardService.update(boardDto);
			return "redirect:/board/list";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	@PostMapping("/delete")
	public String delete(@RequestParam("postId") String postId) {
		try {
			boardService.deleteByCode(postId);
			return "redirect:/board/list";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
}
