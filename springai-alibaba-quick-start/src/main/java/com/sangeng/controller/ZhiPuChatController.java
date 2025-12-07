package com.sangeng.controller;

import lombok.RequiredArgsConstructor;
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
    public String chat(@RequestParam(name = "prompt") String prompt) {
        return chatModel.call(prompt);
    }
}
