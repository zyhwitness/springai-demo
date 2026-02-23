# springai-demo

**项目概览**
- 这是一个 Maven 多模块工程，统一管理 Spring Boot 3.4、Spring AI 与 Spring AI Alibaba 版本（见 [pom.xml](file:///Users/iwitness/Desktop/springai-demo/pom.xml#L1-L65)）。
- 模块包括：springai-alibaba-quick-start、springai-alibaba-rag、springai-alibaba-mcp-server、spingai-alibaba-graph。
- 从依赖与示例代码看，这是围绕 Spring AI + 智谱模型 + Alibaba AI 生态的学习型示例工程。

**模块用途与知识点**
- **springai-alibaba-quick-start**：基础对话与提示工程入门。
    - ChatModel/ChatClient 基本调用、系统消息/用户消息、选项配置、流式输出（见 [ZhiPuChatController](file:///Users/iwitness/Desktop/springai-demo/springai-alibaba-quick-start/src/main/java/com/sangeng/controller/ZhiPuChatController.java#L1-L71)）。
    - ChatClient 自定义参数与返回 ChatResponse（见 [ZhiPuChatController](file:///Users/iwitness/Desktop/springai-demo/springai-alibaba-quick-start/src/main/java/com/sangeng/controller/ZhiPuChatController.java#L42-L70)）。
    - Advisor 机制与会话记忆（MessageChatMemoryAdvisor / 自定义内存 Advisor）（见 [AdvisorController](file:///Users/iwitness/Desktop/springai-demo/springai-alibaba-quick-start/src/main/java/com/sangeng/controller/AdvisorController.java#L1-L50)、[ZhiPuChatMemoryController](file:///Users/iwitness/Desktop/springai-demo/springai-alibaba-quick-start/src/main/java/com/sangeng/controller/ZhiPuChatMemoryController.java#L1-L65)）。
- **springai-alibaba-rag**：RAG 落地示例。
    - CSV 数据导入向量库、向量检索与 RetrievalAugmentationAdvisor（见 [CoffeeController](file:///Users/iwitness/Desktop/springai-demo/springai-alibaba-rag/src/main/java/com/sangeng/controller/CoffeeController.java#L1-L114)）。
    - Redis 向量存储、RAG 依赖、MCP Client（见 [springai-alibaba-rag/pom.xml](file:///Users/iwitness/Desktop/springai-demo/springai-alibaba-rag/pom.xml#L1-L48)）。
    - 相关配置在 application.yaml（向量库与模型接入等）（见 [springai-alibaba-rag/application.yaml](file:///Users/iwitness/Desktop/springai-demo/springai-alibaba-rag/src/main/resources/application.yaml#L1-L44)）。
- **springai-alibaba-mcp-server**：MCP 工具服务端示例。
    - 通过 @Tool 暴露工具能力，并作为 MCP Server 对外提供（见 [TimeTools](file:///Users/iwitness/Desktop/springai-demo/springai-alibaba-mcp-server/src/main/java/com/sangeng/tool/TimeTools.java#L1-L20)）。
    - 工具回调注册（见 [McpConfig](file:///Users/iwitness/Desktop/springai-demo/springai-alibaba-mcp-server/src/main/java/com/sangeng/config/McpConfig.java#L1-L19)）。
- **spingai-alibaba-graph**：基于 Alibaba Graph 的工作流/Agent 图示例。
    - 图的定义、条件边、循环边、异步节点（见 [GraphConfig](file:///Users/iwitness/Desktop/springai-demo/spingai-alibaba-graph/src/main/java/com/sangeng/config/GraphConfig.java#L1-L137)）。
    - 图执行入口与 REST 接口（见 [GraphController](file:///Users/iwitness/Desktop/springai-demo/spingai-alibaba-graph/src/main/java/com/sangeng/controller/GraphController.java#L1-L78)）。
    - 节点示例（造句、翻译、笑话生成与评估）（见 [SentenceConstructionNode](file:///Users/iwitness/Desktop/springai-demo/spingai-alibaba-graph/src/main/java/com/sangeng/node/SentenceConstructionNode.java#L1-L42)、[TranslationNode](file:///Users/iwitness/Desktop/springai-demo/spingai-alibaba-graph/src/main/java/com/sangeng/node/TranslationNode.java#L1-L42)、[GenerateJokeNode](file:///Users/iwitness/Desktop/springai-demo/spingai-alibaba-graph/src/main/java/com/sangeng/node/GenerateJokeNode.java#L1-L41)、[EvaluateJokesNode](file:///Users/iwitness/Desktop/springai-demo/spingai-alibaba-graph/src/main/java/com/sangeng/node/EvaluateJokesNode.java#L1-L43)）。

**实战中更重要的部分**
- RAG：数据摄取、向量化、检索增强是最常见的企业落地路径（见 [CoffeeController](file:///Users/iwitness/Desktop/springai-demo/springai-alibaba-rag/src/main/java/com/sangeng/controller/CoffeeController.java#L1-L114)）。
- MCP：工具调用/函数调用让模型具备“行动能力”，适合与业务系统集成（见 [TimeTools](file:///Users/iwitness/Desktop/springai-demo/springai-alibaba-mcp-server/src/main/java/com/sangeng/tool/TimeTools.java#L1-L20)）。
- Graph：多步骤流程编排、条件分支与循环控制，适合复杂 Agent 工作流（见 [GraphConfig](file:///Users/iwitness/Desktop/springai-demo/spingai-alibaba-graph/src/main/java/com/sangeng/config/GraphConfig.java#L1-L137)）。
- Quick-start：基础知识必备，但更多是入门演示与 API 试用（见 [ZhiPuChatController](file:///Users/iwitness/Desktop/springai-demo/springai-alibaba-quick-start/src/main/java/com/sangeng/controller/ZhiPuChatController.java#L1-L71)）。

**是否是 Spring AI 学习项目**
- 是。模块划分覆盖了 Spring AI 的核心学习路径：基础对话、RAG、MCP 工具、Graph 工作流，符合典型学习/示例项目特征（见 [pom.xml](file:///Users/iwitness/Desktop/springai-demo/pom.xml#L1-L65)）。