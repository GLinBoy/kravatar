package com.glinboy.kavatar.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeResource {

	@GetMapping
	public String getHomePage() {
		return "index";
	}


	@GetMapping("/{id}")
	public String getProfilePage(@PathVariable String id) {
		return "profile";
	}

}
