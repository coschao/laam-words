package com.laam.words.app.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CommonController {

	@GetMapping("/common/datetime") //@RequestMapping("/common/datetime")
	@ResponseBody
	public String datetime() {
		String dt = currentDatetime();

		return String.format("%s", dt);
	}

	
    @GetMapping("hello")
    public String hello(Model model) {
    	log.info("Hello Controller 호출됨, INFO 레벨 로그"); // INFO 레벨 로그 출력
        log.debug("디버그 로그입니다.");
        String datetime = currentDatetime();

        model.addAttribute("datetime", datetime);
        return "hello";
    }

	public static String currentDatetime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		LocalDateTime now = LocalDateTime.now();
		String dt = now.format(dtf);

		return String.format("%s", dt);
	}
}
