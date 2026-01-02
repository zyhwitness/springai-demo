package com.sangeng.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.*;

public class SimpleMessageChatMemoryAdvisor implements BaseAdvisor {

    // 本案例主要是为了学习和理解如何写Advisor所以这里选用最简单的实现。实际肯定不能这样存储消息
    private static Map<String, List<Message>> chatMemory = new HashMap<>();

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {

        // 通过会话id查询之前的对话记录
        String sessionId = "test";
        List<Message> messages = chatMemory.get(sessionId);
        if (messages == null) {
            messages = new ArrayList<>();
        }

        // 把这次请求的消息添加到对话记录中
        List<Message> newMessageList = chatClientRequest.prompt().getInstructions();
        messages.addAll(newMessageList);
        chatMemory.put(sessionId, messages);

        // 把添加后记录的List<Message> 放入请求中
        Prompt newPrompt = chatClientRequest.prompt().mutate()
                .messages(messages)
                .build();
        ChatClientRequest newRequest = chatClientRequest.mutate()
                .prompt(newPrompt)
                .build();
        return newRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {

        // 通过会话id查询之前的对话记录
        String sessionId = "test";
        List<Message> messages = chatMemory.get(sessionId);
        if (messages == null) {
            messages = new ArrayList<>();
        }

        // 获取response中ai的消息 添加到对话记录中
        if (Objects.isNull(chatClientResponse)) {
            return chatClientResponse;
        }
        AssistantMessage assistantMessage = chatClientResponse.chatResponse().getResult().getOutput();
        messages.add(assistantMessage);
        chatMemory.put(sessionId, messages);
        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
