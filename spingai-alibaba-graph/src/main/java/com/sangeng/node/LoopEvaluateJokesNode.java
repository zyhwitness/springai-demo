package com.sangeng.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Map;

/**
 * 循环评估笑话节点
 * 实现了NodeAction接口，作为Graph工作流中的一个处理节点
 */
public class LoopEvaluateJokesNode implements NodeAction {

    private final ChatClient chatClient;
    private final Integer maxLoopCount;
    private final Integer maxScore;

    public LoopEvaluateJokesNode(ChatClient.Builder chatClient, Integer maxLoopCount, Integer maxScore) {
        this.chatClient = chatClient.build();
        this.maxLoopCount = maxLoopCount;
        this.maxScore = maxScore;
    }

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {

        // 从状态中获取
        String joke = state.value("joke", "");
        Integer loopCount = state.value("loopCount", 1);

        // 创建提示模板，定义AI任务
        PromptTemplate promptTemplate = new PromptTemplate("你是一个笑话评分专家，能够对笑话进行评分，基于效果的搞笑程度给出0到10分的打分。" +
                "要求打分只能是整数。要求结果只返回最后的打分，不要其他内容。" +
                "要评分的笑话: {joke}");

        // 为模板添加参数
        promptTemplate.add("joke", joke);

        // 执行AI调用并获取结果
        String content = chatClient.prompt().user(promptTemplate.render()).call().content();

        Integer score = Integer.valueOf(content.trim());

        String result = "loop";
        if (score > maxScore || loopCount > maxLoopCount) {
            result = "break";
        }

        loopCount++;

        // 将结果保存到工作流状态中
        return Map.of("result", result,
                "loopCount", loopCount);
    }
}
