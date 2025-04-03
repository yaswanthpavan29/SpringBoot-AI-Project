package com.ai.springboot_AI.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ai.springboot_AI.service.OllamaService;

@Controller
public class OllamaController {
	
	OllamaService service;
	
	public OllamaController(OllamaService service) {
		this.service=service;
	}
	
	@GetMapping("/chat")
	public String getIndexPage() {
		return "Index";
	}
	@GetMapping("/start-chat")
	public String getChatPage() {
		return "Chatpage";
	}
	@PostMapping("/send-message")
	public String getResponse(@RequestParam("message") String message,Model model) {
		String response=service.getOllamaResponse(message);
		model.addAttribute("userMessage", message);
        model.addAttribute("aiResponse", response);
        return "Chatpage";
	}

}
