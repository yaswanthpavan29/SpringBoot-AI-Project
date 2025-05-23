package com.ai.springboot_AI.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.springframework.stereotype.Service;

@Service
public class OllamaService {
	public String getOllamaResponse(String userMessage) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ollama", "run", "llama3.2:1b");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
            writer.write(userMessage + "\n");
            writer.flush();
            writer.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder aiResponse = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                aiResponse.append(stripAnsiCodes(line)).append(" ");
            }

            process.waitFor();
            return aiResponse.toString().trim();
        } catch (Exception e) {
            return "Error processing request: " + e.getMessage();
        }
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
