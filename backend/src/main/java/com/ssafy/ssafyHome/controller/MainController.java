package com.ssafy.ssafyHome.controller;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//@Slf4j
@Controller
public class MainController {
	@GetMapping("/")
	public String main() {
		System.out.println("메인");
		return "index";
	}
}
