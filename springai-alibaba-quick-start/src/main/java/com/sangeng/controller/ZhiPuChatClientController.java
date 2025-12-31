package com.sangeng.controller;

import com.sangeng.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

//@RequiredArgsConstructor
@RestController
@RequestMapping("/chatclient")
public class ZhiPuChatClientController {

    private final ChatClient chatClient;

    public ZhiPuChatClientController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "query") String query) {
        SystemMessage systemMessage = new SystemMessage("你是一个有用的AI助手");
        UserMessage userMessage = new UserMessage(query);
        ZhiPuAiChatOptions chatOptions = ZhiPuAiChatOptions.builder()
                .model("glm-4.5")
                .maxTokens(15536)
                .temperature(0.0)
                .build();
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), chatOptions);
        return chatClient.prompt(prompt)
                .call().content();
    }

    @GetMapping("/chat2")
    public String chat2(@RequestParam(name = "query") String query) {

        ZhiPuAiChatOptions chatOptions = ZhiPuAiChatOptions.builder()
                .model("glm-4.5")
                .maxTokens(15536)
                .temperature(0.0)
                .build();

        return chatClient.prompt()
                .system("你是一个有用的AI助手")
                .user(query)
                .options(chatOptions)
                .call().content();
    }

    @GetMapping("/chat3")
    public ChatResponse chat3(@RequestParam(name = "query") String query) {

        ZhiPuAiChatOptions chatOptions = ZhiPuAiChatOptions.builder()
                .model("glm-4.5")
                .maxTokens(15536)
                .temperature(0.0)
                .build();

        return chatClient.prompt()
                .system("你是一个有用的AI助手")
                .user(query)
                .options(chatOptions)
                .call().chatResponse();
    }

    @GetMapping("/response")
    public Book response() {
        return chatClient.prompt()
                .user("给我随机生成一本书，要求书名和作者都是中文")
                .call().entity(Book.class);
    }

    @GetMapping("/stream")
    public Flux<String> stream() {
        return chatClient.prompt()
                .user("给我随机生成一本书，要求书名和作者都是中文")
                .stream().content();
    }

}
