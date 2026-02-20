package com.sangeng.config;

import com.alibaba.cloud.ai.graph.*;
import com.alibaba.cloud.ai.graph.action.AsyncNodeAction;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.sangeng.node.SentenceConstructionNode;
import com.sangeng.node.TranslationNode;
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
}
