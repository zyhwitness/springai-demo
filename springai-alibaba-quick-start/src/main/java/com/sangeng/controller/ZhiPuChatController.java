package com.sangeng.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
