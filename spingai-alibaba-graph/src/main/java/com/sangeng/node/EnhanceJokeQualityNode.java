package com.sangeng.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Map;

/**
 * 优化笑话节点
 * 实现了NodeAction接口，作为Graph工作流中的一个处理节点
 */
public class EnhanceJokeQualityNode implements NodeAction {

    private final ChatClient chatClient;

    public EnhanceJokeQualityNode(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {

        // 从状态中获取
        String joke = state.value("joke", "");

        // 创建提示模板，定义AI任务
        PromptTemplate promptTemplate = new PromptTemplate("你是一个笑话优化专家，你能够优化笑话，让它更加搞笑。" +
                "要求只返回翻译的结果不要返回其他信息。要优化的笑话: {joke}");

        // 为模板添加参数
        promptTemplate.add("joke", joke);

        // 执行AI调用并获取结果
        String content = chatClient.prompt().user(promptTemplate.render()).call().content();

        // 将结果保存到工作流状态中
        return Map.of("newJoke", content);
    }
}
