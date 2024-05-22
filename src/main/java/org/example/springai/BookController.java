package org.example.springai;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.ai.parser.MapOutputParser;
import org.springframework.ai.parser.OutputParser;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {
    private final ChatClient chatClient;

    public BookController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/by-author")
    public Author getBooksByAuthor(@RequestParam(value = "author", defaultValue = "Ken Kousen") String author) {
        String promptMessage = """
                Generate a list of books written by the author {author}.
                If you aren't positive that a book belongs to this author please don't include it.
                                
                {format}
                """;

        OutputParser<Author> outputParser = new BeanOutputParser<>(Author.class);
        String format = outputParser.getFormat();
        PromptTemplate promptTemplate = new PromptTemplate(
                promptMessage,
                Map.of(
                        "author", author,
                        "format", format
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

    @GetMapping("/author/{author}")
    public Map<String, Object> getAuthorsSocialLinks(@PathVariable String author) {
        String promptMessage = """
                Generate a list of links for the author {author}.
                Include the authors name as the key and any social network links as the object.
                                
                {format}
                """;

        OutputParser<Map<String, Object>> outputParser = new MapOutputParser();
        String format = outputParser.getFormat();
        PromptTemplate promptTemplate = new PromptTemplate(
                promptMessage,
                Map.of(
                        "author", author,
                        "format", format
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
