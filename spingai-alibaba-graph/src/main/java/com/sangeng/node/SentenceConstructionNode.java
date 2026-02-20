package com.sangeng.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Map;

/**
 * 句子构造节点
 * 该节点负责根据用户输入的单词生成包含该单词的英文句子
 * 实现了NodeAction接口，作为Graph工作流中的一个处理节点
 */
public class SentenceConstructionNode implements NodeAction {

    private final ChatClient chatClient;

    public SentenceConstructionNode(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {

        // 从状态中获取用户输入的单词，如果没有则使用空字符串
        String word = state.value("word", "");

        // 创建提示模板，定义AI任务
        PromptTemplate promptTemplate = new PromptTemplate("你是一个英语造句专家，能够基于给定的单词进行造句。" +
                "要求只返回最终造好的句子，不要返回其他信息。 给定的单词: {word}");

        // 为模板添加参数
        promptTemplate.add("word", word);

        // 执行AI调用并获取结果
        String content = chatClient.prompt().user(promptTemplate.render()).call().content();

        // 将结果保存到工作流状态中
        return Map.of("sentence", content);
    }
}
