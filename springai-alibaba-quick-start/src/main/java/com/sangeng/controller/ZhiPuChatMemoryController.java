package com.sangeng.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//@RequiredArgsConstructor
@RestController
@RequestMapping("/chatmemory")
public class ZhiPuChatMemoryController {

    private final ChatClient chatClient;

    // 构造器注入
    public ZhiPuChatMemoryController(ChatClient.Builder builder) {
        // 创建 MessageWindowChatMemory
        MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory
                .builder()
                //TODO 如何自定义chatMemoryRepository，从而将消息存储在mysql或redis中？
//                .maxMessages()
//                .chatMemoryRepository()
                .build();

        //创建 MessageChatMemoryAdvisor
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor
                .builder(messageWindowChatMemory)
                .build();

        this.chatClient = builder
                .defaultAdvisors(messageChatMemoryAdvisor)
                .build();
    }

    @GetMapping("/messageChatMemory")
    public String messageChatMemory(@RequestParam(name = "query") String query, @RequestParam(name = "sessionId") String sessionId) {
        return chatClient.prompt()
                .user(query)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, sessionId))
                .call().content();
    }

    public static void main(String[] args) {

        // 创建UserMessage
        PromptTemplate userPrompt = new PromptTemplate("你是一个有用的人工智能助手，名字是{name}请用{voice}的风格回答以下问题：{userQuestion}");
        Message message = userPrompt.createMessage(Map.of("name", "小白", "voice", "幽默", "userQuestion", "推荐上海的三个景点"));
        System.out.println(message);

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate("你是一个有用的人工智能助手，名字是{name}请用{voice}的风格回答以下问题：{userQuestion}");
        Message message2 = systemPromptTemplate.createMessage(Map.of("name", "小白", "voice", "幽默", "userQuestion", "推荐上海的三个景点"));
        System.out.println(message2);

    }

}
