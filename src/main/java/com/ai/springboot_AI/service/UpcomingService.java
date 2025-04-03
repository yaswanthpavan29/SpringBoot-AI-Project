package com.ai.springboot_AI.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
@Service
public class UpcomingService {
	public SseEmitter streamOllamaResponse(String userMessage) {
        SseEmitter emitter = new SseEmitter();

        new Thread(() -> {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("ollama", "run", "llama3.2:1b");
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();

                OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
                writer.write(userMessage + "\n");
                writer.flush();
                writer.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    // Clean output (remove unnecessary "FLUSH" or ANSI characters)
                    String cleanedText = stripAnsiCodes(line);

                    // Stream response word-by-word
                    for (String word : cleanedText.split("\\s+")) {
                        emitter.send(word + " ");
                        Thread.sleep(50); // Simulated typing effect
                    }
                }

                emitter.complete(); // Complete SSE stream
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }).start();

        return emitter;
    }

    private String stripAnsiCodes(String text) {
        String ansiRegex = "\\x1B\\[[0-?]*[ -/]*[@-~]";
        text = text.replaceAll(ansiRegex, "");

        String controlCharsRegex = "[\\p{Cntrl}&&[^\r\n\t]]";
        text = text.replaceAll(controlCharsRegex, "");

        String brailleRegex = "[⠁-⣿]";
        text = text.replaceAll(brailleRegex, "");

        return text.replaceAll("\\s+", " ").trim();
    }
}
