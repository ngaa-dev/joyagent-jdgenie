package com.jd.genie.llm;

import com.alibaba.fastjson.JSONObject;
import com.jd.genie.BaseUtils;
import com.jd.genie.agent.agent.AgentContext;
import com.jd.genie.agent.dto.Memory;
import com.jd.genie.agent.dto.Message;
import com.jd.genie.agent.dto.tool.ToolCall;
import com.jd.genie.agent.dto.tool.ToolChoice;
import com.jd.genie.agent.enums.RoleType;
import com.jd.genie.agent.llm.LLM;
import com.jd.genie.agent.printer.LogPrinter;
import com.jd.genie.agent.printer.Printer;
import com.jd.genie.agent.prompt.ToolCallPrompt;
import com.jd.genie.agent.tool.BaseTool;
import com.jd.genie.agent.tool.ToolCollection;
import com.jd.genie.agent.util.DateUtil;
import com.jd.genie.agent.util.SpringContextHolder;
import com.jd.genie.config.GenieConfig;
import com.jd.genie.model.req.AgentRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@SpringBootTest
public class LLMTest {
    @Test
    public void callOpenAIFunctionCallStreamTest() throws ExecutionException, InterruptedException {

        GenieConfig genieConfig = SpringContextHolder.getApplicationContext().getBean(GenieConfig.class);

        String query = "查询今天是几月几号？星期几，推荐下今天吃什么，以及做法？";
        String modelName = "gpt-4.1";
        modelName = "volcengine/deepseek-r1-250528";
        modelName = "gpt-5";
        modelName = "claude-sonnet-4-20250514";
        String llmErp = "";

        LLM llm = new LLM(modelName, llmErp);

        AgentContext agentContext = new AgentContext();
        agentContext.setQuery(query);
        agentContext.setSopPrompt(genieConfig.getGenieSopPrompt());
        agentContext.setBasePrompt("");
        agentContext.setIsStream(Boolean.FALSE);
        agentContext.setDateInfo(DateUtil.CurrentDateInfo());
        // 普通模式是 3
        // 深度搜索是 5
        agentContext.setAgentType(3);

        AgentRequest request = new AgentRequest();
        request.setRequestId(UUID.randomUUID().toString());
        Printer printer = new LogPrinter(request);
        agentContext.setPrinter(printer);

        ToolCollection toolCollection = BaseUtils.buildToolCollection(agentContext, genieConfig);
        agentContext.setToolCollection(toolCollection);

        StringBuilder toolPrompt = new StringBuilder();
        for (BaseTool tool : toolCollection.getToolMap().values()) {
            toolPrompt.append(String.format("工具名：%s 工具描述：%s\n", tool.getName(), tool.getDescription()));
        }

        String systemPrompt = genieConfig.getReactSystemPromptMap().getOrDefault("default", ToolCallPrompt.SYSTEM_PROMPT)
                .replace("{{tools}}", toolPrompt.toString())
                .replace("{{query}}", agentContext.getQuery())
                .replace("{{date}}", agentContext.getDateInfo())
                .replace("{{basePrompt}}", agentContext.getBasePrompt());

        Memory memory = new Memory();
        BaseUtils.updateMemory(memory, RoleType.USER, query, null);
        CompletableFuture<LLM.ToolCallResponse> toolCallResponseCompletableFuture = llm.askTool(agentContext,
                memory.getMessages(),
                Message.systemMessage(systemPrompt, null),
                toolCollection, ToolChoice.AUTO, null, Boolean.TRUE, 300);
        LLM.ToolCallResponse toolCallResponse = toolCallResponseCompletableFuture.get();
        for (ToolCall toolCall : toolCallResponse.getToolCalls()) {
            System.out.println("toolCall to json String: " + JSONObject.toJSONString(toolCall));
        }
    }


}