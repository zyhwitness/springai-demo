package com.sangeng.controller;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.OverAllState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/graph")
public class GraphController {

    private static final Logger log = LoggerFactory.getLogger(GraphController.class);
    private final CompiledGraph compiledGraph;
    private final CompiledGraph simpleGraph;
    private final CompiledGraph conditionalGraph;

    public GraphController(@Qualifier("quickStartGraph") CompiledGraph compiledGraph, @Qualifier("simpleGraph") CompiledGraph simpleGraph, @Qualifier("conditionalGraph") CompiledGraph conditionalGraph) {
        this.compiledGraph = compiledGraph;
        this.simpleGraph = simpleGraph;
        this.conditionalGraph = conditionalGraph;
    }

    @GetMapping("/quickStartGraph")
    public String quickStartGraph() {
        Optional<OverAllState> optionalOverAllState = compiledGraph.call(Map.of());
        log.info("optionalOverAllState: {}", optionalOverAllState);
        return "ok";
    }

    @GetMapping("/simpleGraph")
    public Map<String, Object> simpleGraph(@RequestParam("word") String word) {
        try {
            Optional<OverAllState> optionalOverAllState = simpleGraph.call(Map.of("word", word));
            log.info("optionalOverAllState: {}", optionalOverAllState);
            return optionalOverAllState.map(OverAllState::data).orElse(Map.of());
        } catch (Exception e) {
            log.error("Error executing simpleGraph", e);
            return Map.of("error", e.getMessage());
        }
    }

    @GetMapping("/conditionalGraph")
    public Map<String, Object> conditionalGraph(@RequestParam("topic") String topic) {
        try {
            Optional<OverAllState> optionalOverAllState = conditionalGraph.call(Map.of("topic", topic));
            log.info("optionalOverAllState: {}", optionalOverAllState);
            return optionalOverAllState.map(OverAllState::data).orElse(Map.of());
        } catch (Exception e) {
            log.error("Error executing conditionalGraph", e);
            return Map.of("error", e.getMessage());
        }
    }
}
