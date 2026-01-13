package com.sangeng.config;

import com.sangeng.tool.TimeTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 配置工具对外提供
 **/
@Configuration
public class McpConfig {

    public ToolCallbackProvider toolCallbackProvider(TimeTools timeTools) {
        return MethodToolCallbackProvider.builder().toolObjects(timeTools).build();
    }
}
