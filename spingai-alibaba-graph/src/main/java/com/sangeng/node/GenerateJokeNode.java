package com.sangeng.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Map;

/**
 * 生成笑话节点
 * 实现了NodeAction接口，作为Graph工作流中的一个处理节点
 */
public class GenerateJokeNode implements NodeAction {

    private final ChatClient chatClient;

    public GenerateJokeNode(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {

        // 从状态中获取
        String topic = state.value("topic", "");

        // 创建提示模板，定义AI任务
        PromptTemplate promptTemplate = new PromptTemplate("你需要写一个关于指定主题的短笑话。" +
                "要求返回的结果中只能包含笑话的内容主题: {topic}");

        // 为模板添加参数
        promptTemplate.add("topic", topic);

        // 执行AI调用并获取结果
        String content = chatClient.prompt().user(promptTemplate.render()).call().content();

        // 将结果保存到工作流状态中
        return Map.of("joke", content);
    }
}
