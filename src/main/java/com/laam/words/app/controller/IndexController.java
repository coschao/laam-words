package com.laam.words.app.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

	@GetMapping("/datetime")
	public String datetime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		LocalDateTime now = LocalDateTime.now();
		String dt = now.format(dtf);
		
		return String.format("%s", dt);
	}

	@GetMapping("/other/datetime")
	public String otherDatetime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		LocalDateTime now = LocalDateTime.now();
		String dt = now.format(dtf);
		
		return String.format("%s", dt);
	}

}
