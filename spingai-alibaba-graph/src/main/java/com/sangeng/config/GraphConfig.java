package com.sangeng.config;

import com.alibaba.cloud.ai.graph.*;
import com.alibaba.cloud.ai.graph.action.AsyncEdgeAction;
import com.alibaba.cloud.ai.graph.action.AsyncNodeAction;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.sangeng.node.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class GraphConfig {

    private static final Logger log = LoggerFactory.getLogger(GraphConfig.class);

    @Bean("quickStartGraph")
    public CompiledGraph quickStartGraph() throws GraphStateException {

        // 定义状态图（状态，节点，边）

        // 创建KeyStrategyFactory（ReplaceStrategy -> 替换策略）
        KeyStrategyFactory keyStrategyFactory = () -> Map.of("input1", new ReplaceStrategy(),
                "input2", new ReplaceStrategy());

        // 定义状态
        StateGraph stateGraph = new StateGraph("quickStartGraph", keyStrategyFactory);

        // 定义节点
        stateGraph.addNode("node1", AsyncNodeAction.node_async(new NodeAction() {
            @Override
            public Map<String, Object> apply(OverAllState state) throws Exception {
                log.info("node1 state: {}", state);
                return Map.of("input1", 1,
                        "input2", 1);
            }
        }));
        stateGraph.addNode("node2", AsyncNodeAction.node_async(new NodeAction() {
            @Override
            public Map<String, Object> apply(OverAllState state) throws Exception {
                log.info("node2 state: {}", state);
                return Map.of("input1", 2,
                        "input2", 2);
            }
        }));

        // 定义边
        stateGraph.addEdge(StateGraph.START, "node1");
        stateGraph.addEdge("node1", "node2");
        stateGraph.addEdge("node2", StateGraph.END);

        // 编译生成CompiledGraph后注入Spring容器
        return stateGraph.compile();

    }

    @Bean("simpleGraph")
    public CompiledGraph simpleGraph(ChatClient.Builder chatClient) throws GraphStateException {
        KeyStrategyFactory keyStrategyFactory = () -> Map.of("word", new ReplaceStrategy(),
                "sentence", new ReplaceStrategy(),
                "translation", new ReplaceStrategy());
        // 定义状态
        StateGraph stateGraph = new StateGraph("simpleGraph", keyStrategyFactory);

        stateGraph.addNode("sentence_construction", AsyncNodeAction.node_async(new SentenceConstructionNode(chatClient)));
        stateGraph.addNode("translation", AsyncNodeAction.node_async(new TranslationNode(chatClient)));

        stateGraph.addEdge(StateGraph.START, "sentence_construction");
        stateGraph.addEdge("sentence_construction", "translation");
        stateGraph.addEdge("translation", StateGraph.END);

        return stateGraph.compile();

    }

    /**
     * 创建一个条件边图
     *
     * @param chatClient
     * @return
     * @throws GraphStateException
     */
    @Bean("conditionalGraph")
    public CompiledGraph conditionalGraph(ChatClient.Builder chatClient) throws GraphStateException {
        KeyStrategyFactory keyStrategyFactory = () -> Map.of("topic", new ReplaceStrategy());
        // 定义状态
        StateGraph stateGraph = new StateGraph("conditionalGraph", keyStrategyFactory);

        stateGraph.addNode("generate_joke", AsyncNodeAction.node_async(new GenerateJokeNode(chatClient)));
        stateGraph.addNode("evaluate_joke", AsyncNodeAction.node_async(new EvaluateJokesNode(chatClient)));
        stateGraph.addNode("enhance_joke_quality", AsyncNodeAction.node_async(new EnhanceJokeQualityNode(chatClient)));

        stateGraph.addEdge(StateGraph.START, "generate_joke");
        stateGraph.addEdge("generate_joke", "evaluate_joke");
        // 条件边，拿评估节点后状态中的result字段判断，如果result为优秀则结束，否则进入增强节点
        stateGraph.addConditionalEdges("evaluate_joke", AsyncEdgeAction.edge_async(new EdgeAction() {
            @Override
            public String apply(OverAllState state) throws Exception {
                return state.value("result", "优秀");
            }
        }), Map.of("优秀", StateGraph.END,
                "不够优秀", "enhance_joke_quality"));
        stateGraph.addEdge("enhance_joke_quality", StateGraph.END);

        return stateGraph.compile();

    }

    @Bean("loopGraph")
    public CompiledGraph loopGraph(ChatClient.Builder chatClient) throws GraphStateException {
        KeyStrategyFactory keyStrategyFactory = () -> Map.of("topic", new ReplaceStrategy());
        // 定义状态
        StateGraph stateGraph = new StateGraph("loopGraph", keyStrategyFactory);
        stateGraph.addNode("generate_joke", AsyncNodeAction.node_async(new GenerateJokeNode(chatClient)));
        stateGraph.addNode("loop_evaluate_joke", AsyncNodeAction.node_async(new LoopEvaluateJokesNode(chatClient, 3, 7)));

        stateGraph.addEdge(StateGraph.START, "generate_joke");
        stateGraph.addEdge("generate_joke", "loop_evaluate_joke");

        stateGraph.addConditionalEdges("loop_evaluate_joke", AsyncEdgeAction.edge_async(new EdgeAction() {
            @Override
            public String apply(OverAllState state) throws Exception {
                return state.value("result", "break");
            }
        }), Map.of("break", StateGraph.END,
                "loop", "generate_joke"));

        return stateGraph.compile();
    }
}
