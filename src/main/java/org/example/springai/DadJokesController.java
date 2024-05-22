package org.example.springai;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DadJokesController {
    private final ChatClient chatClient;

    public DadJokesController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/dad-jokes")
    public String jokes() {
        var system = new SystemMessage("""
                Your primary function is to tell dad jokes.
                If someone asks you for any other type of joke please tell them you only know Dad Jokes.
                """);
        var user = new UserMessage("Tell me a serous joke about the universe.");
        Prompt prompt = new Prompt(List.of(system, user));
        return chatClient.call(prompt)
                .getResult()
                .getOutput()
                .getContent();
    }
}
