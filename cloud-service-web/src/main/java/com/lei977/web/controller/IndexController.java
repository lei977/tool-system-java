package com.lei977.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demo")
public class IndexController {

	@GetMapping("test")
	public String test() {
		return "test";
	}
}
