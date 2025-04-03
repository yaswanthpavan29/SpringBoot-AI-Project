package com.ai.springboot_AI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ai.springboot_AI.service.UpcomingService;
@Controller
@RequestMapping("/v2")
public class UpcomingController {
	UpcomingService service;
	@Autowired
	public UpcomingController(UpcomingService service) {
		this.service=service;
	}
	@GetMapping("/chat")
	public String getIndexPage() {
		return "UpcomingIndex";
	}
	@GetMapping("/start-chat")
	public String getChatPage() {
		return "test";
	}
	
	 @GetMapping("/stream-response")
	    public SseEmitter streamChatResponse(@RequestParam("message") String message) {
	        return service.streamOllamaResponse(message);
	    }

}
