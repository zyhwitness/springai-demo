package com.sangeng.controller;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/coffee")
public class CoffeeController {

    private final VectorStore vectorStore;

    public CoffeeController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @GetMapping("/import")
    public String importCSV() {
        try {
            // 读取classpath下的QA.csv文件
            ClassPathResource resource = new ClassPathResource("QA.csv");
            InputStreamReader reader = new
                    InputStreamReader(resource.getInputStream());
            // 使用Apache Commons CSV解析CSV文件
            CSVParser csvParser = CSVFormat.DEFAULT
                    .builder()
                    .setHeader() // 第一行作为标题
                    .setSkipHeaderRecord(true) // 跳过标题行
                    .build()
                    .parse(reader);
            List<Document> documents = new ArrayList<>();
            // 遍历每一行记录
            for (CSVRecord record : csvParser) {
                // 获取问题和回答字段
                String question = record.get("问题");
                String answer = record.get("回答");
                // 将问题和回答组合成文档内容
                String content =
                        "问题: " + question + "\n回答: " + answer;
                // 创建Document对象
                Document document = new Document(content);
                // 添加到文档列表
                documents.add(document);
            }
            // 关闭解析器
            csvParser.close();
            // 将文档存入向量数据库
            vectorStore.add(documents);
            return "成功导入 " + documents.size() + " 条记录到向量数据库";
        } catch (IOException e) {
            e.printStackTrace();
            return "导入失败: " + e.getMessage();
        }
    }
}
