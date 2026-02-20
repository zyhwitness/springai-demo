package com.sangeng.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Map;

/**
 * 翻译节点
 * 该节点负责将英文句子翻译成中文
 * 实现了NodeAction接口，作为Graph工作流中的一个处理节点
 */
public class TranslationNode implements NodeAction {

    private final ChatClient chatClient;

    public TranslationNode(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {

        // 从状态中获取英文句子，如果没有则使用空字符串
        String sentence = state.value("sentence", "");

        // 创建提示模板，定义AI任务
        PromptTemplate promptTemplate = new PromptTemplate("你是一个英语翻译专家，能够对句子进行翻译。" +
                "要求只返回翻译的结果不要返回其他信息。要翻译的句子: {sentence}");

        // 为模板添加参数
        promptTemplate.add("sentence", sentence);

        // 执行AI调用并获取结果
        String content = chatClient.prompt().user(promptTemplate.render()).call().content();

        // 将结果保存到工作流状态中
        return Map.of("translation", content);
    }
}
