package com.sangeng.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/zhipu")
public class ZhiPuChatController {

    private final ChatModel chatModel;

    @GetMapping("/hello")
    public String hello() {
        return "hello zhipu";
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "query") String query) {
        return chatModel.call(query);
    }

    @GetMapping("/message")
    public String message(@RequestParam(name = "message") String message) {
        SystemMessage systemMessage = new SystemMessage("你是一个有用的AI助手");
        UserMessage userMessage = new UserMessage(message);
        return chatModel.call(systemMessage, userMessage);
    }

    @GetMapping("/chatOptions")
    public ChatResponse chatOptions(@RequestParam(name = "query") String query) {
        SystemMessage systemMessage = new SystemMessage("你是一个有用的AI助手");
        UserMessage userMessage = new UserMessage(query);
        ZhiPuAiChatOptions zhiPuAiChatOptions = new ZhiPuAiChatOptions();
        zhiPuAiChatOptions.setModel("glm-4.5");
        zhiPuAiChatOptions.setTemperature(0.0);
        zhiPuAiChatOptions.setMaxTokens(15536);
        // 调用模型
        return chatModel.call(new Prompt(List.of(systemMessage, userMessage), zhiPuAiChatOptions));
    }

    @GetMapping("/chatOptions")
    public String chatOptions2(@RequestParam(name = "message") String message) {
        SystemMessage systemMessage = new SystemMessage("你是一个有用的AI助手");
        UserMessage userMessage = new UserMessage(message);
        ZhiPuAiChatOptions chatOptions = ZhiPuAiChatOptions.builder()
                .model("glm-4.5")
                .maxTokens(15536)
                .temperature(0.0)
                .build();
        ChatResponse chatResponse = chatModel.call(new Prompt(List.of(systemMessage, userMessage), chatOptions));
        return chatResponse.getResult().getOutput().getText();
    }
}
