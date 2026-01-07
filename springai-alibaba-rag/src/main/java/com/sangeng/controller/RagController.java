package com.sangeng.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final VectorStore vectorStore;

    public RagController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    /**
     * 数据嵌入
     *
     * @param content
     */
    @PostMapping("/import")
    public String importData(@RequestParam(name = "content") String content) {
        Document document = Document.builder()
                .text(content)
                .build();
        vectorStore.add(List.of(document));
        return "success";
    }
}
