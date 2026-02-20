package com.sangeng.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Map;

/**
 * 评估笑话节点
 * 实现了NodeAction接口，作为Graph工作流中的一个处理节点
 */
public class EvaluateJokesNode implements NodeAction {

    private final ChatClient chatClient;

    public EvaluateJokesNode(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {

        // 从状态中获取
        String joke = state.value("joke", "");

        // 创建提示模板，定义AI任务
        PromptTemplate promptTemplate = new PromptTemplate("你是一个笑话评分专家，能够对笑话进行评分，基于效果的搞笑程度给出0到10分的打分。" +
                "然后基于评分结果进行评价。如果大于等于3分，评价:优秀。否则评价:不够优秀。" +
                "要求结果只返回最后的评价，不要其他内容。" +
                "要求只返回翻译的结果不要返回其他信息。要评分的笑话：:{joke}");

        // 为模板添加参数
        promptTemplate.add("joke", joke);

        // 执行AI调用并获取结果
        String content = chatClient.prompt().user(promptTemplate.render()).call().content();

        // 将结果保存到工作流状态中
        return Map.of("result", content);
    }
}
