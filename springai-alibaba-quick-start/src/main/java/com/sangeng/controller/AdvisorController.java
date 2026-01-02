package com.sangeng.controller;

import com.sangeng.advisor.SGCallAdvisor1;
import com.sangeng.advisor.SGCallAdvisor2;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/advisor")
public class AdvisorController {

    private final ChatClient chatClient;

    public AdvisorController(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
                .build();
    }

    @GetMapping("/simple")
    public String simpleChat(@RequestParam(name = "query") String query) {

        ZhiPuAiChatOptions chatOptions = ZhiPuAiChatOptions.builder()
                .maxTokens(15536)
                .temperature(0.0)
                .model("glm-4.5")
                .build();

        return chatClient.prompt()
                .system("你是一个有用的AI助手。")
                .user(query)
                .advisors(new SGCallAdvisor1(), new SGCallAdvisor2())
                .options(chatOptions)
                .call().content();
    }
}
