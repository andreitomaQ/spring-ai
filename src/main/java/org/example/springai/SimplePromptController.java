package org.example.springai;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimplePromptController {
    private final ChatClient chatClient;

    public SimplePromptController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping()
    public String simple() {
        return chatClient.call(new Prompt("Tell me a dad joke"))
                .getResult()
                .getOutput()
                .getContent();
    }
}
