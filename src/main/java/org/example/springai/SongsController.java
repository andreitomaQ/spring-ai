package org.example.springai;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.ListOutputParser;
import org.springframework.ai.parser.OutputParser;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SongsController {
    private final ChatClient chatClient;

    public SongsController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/songs/basic")
    public String getSongsByArtistBasic(@RequestParam(value = "artist", defaultValue = "Bruno Mars") String artist) {
        var message = """
                Please give me a list of top 10 songs for the artist {artist}. 
                If you don't know, just say "I don't know".
                """;

        PromptTemplate promptTemplate = new PromptTemplate(message, Map.of("artist", artist));
        Prompt prompt = promptTemplate.create();
        return chatClient.call(prompt)
                .getResult()
                .getOutput()
                .getContent();
    }

    @GetMapping("/songs/list")
    public List<String> getSongsByArtistList(@RequestParam(value = "artist", defaultValue = "Bruno Mars") String artist) {
        var message = """
                Please give me a list of top 10 songs for the artist {artist}.
                If you don't know, just say "I don't know".
                                
                {format}
                """;
        OutputParser<List<String>> outputParser = new ListOutputParser(new DefaultConversionService());
        PromptTemplate promptTemplate = new PromptTemplate(
                message,
                Map.of(
                        "artist", artist,
                        "format", outputParser.getFormat()
                )
        );
        Prompt prompt = promptTemplate.create();
        return outputParser.parse(
                chatClient.call(prompt)
                        .getResult()
                        .getOutput()
                        .getContent()
        );
    }
}
