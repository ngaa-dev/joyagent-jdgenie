package com.jd.genie.llm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jd.genie.BaseUtils;
import com.jd.genie.agent.agent.AgentContext;
import com.jd.genie.agent.dto.File;
import com.jd.genie.agent.printer.LogPrinter;
import com.jd.genie.agent.printer.Printer;
import com.jd.genie.agent.tool.ToolCollection;
import com.jd.genie.agent.tool.common.ReportTool;
import com.jd.genie.agent.tool.mcp.McpTool;
import com.jd.genie.agent.util.DateUtil;
import com.jd.genie.agent.util.SpringContextHolder;
import com.jd.genie.config.GenieConfig;
import com.jd.genie.config.McpToolResponse;
import com.jd.genie.config.ToolInfo;
import com.jd.genie.model.req.AgentRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@Slf4j
@SpringBootTest
public class BaseToolTest {

    @Test
    public void showMcpToolTest() {
        String[] mcpServerUrlArr = new String[]{
                "https://mcp.api-inference.modelscope.net/fc30398b2fec46/sse",  // 时间服务
                "https://mcp.api-inference.modelscope.net/826def46ada445/sse",  // 天气服务
                "https://mcp.api-inference.modelscope.net/71fda048172243/sse",  // Markmap思维导图
                "https://mcp.api-inference.modelscope.net/fa7ca04baeee46/sse",  // 菜谱集合
                "https://mcp.api-inference.modelscope.net/5c80b3cd5dcb4d/sse",  // 基金知识库
                "https://mcp.api-inference.modelscope.net/b6f3573401b84d/sse",  // 全网热点趋势

                "https://mcp.api-inference.modelscope.net/8434c8fa5ede41/sse",  // 抓取网页并转换
                "https://mcp.api-inference.modelscope.net/917a8dadf2a14e/sse",  // 中国股票数据
                "https://mcp.api-inference.modelscope.net/0149731c1ae546/sse",  // 12306-MCP车票查询工具
                "https://mcp.api-inference.modelscope.net/0ca53888c0ae4e/sse",  // 世界银行MCP
                "https://mcp.api-inference.modelscope.net/1c0d9aae407b40/sse",  // 发现报告
                "https://mcp.api-inference.modelscope.net/0ce8a72e4fe449/sse",  // 百度地图
                "https://mcp.api-inference.modelscope.net/a8fa69111ede4f/sse"   // 自在招聘
        };

        mcpServerUrlArr = "https://mcp.api-inference.modelscope.net/fc30398b2fec46/sse,https://mcp.api-inference.modelscope.net/826def46ada445/sse,https://mcp.api-inference.modelscope.net/8434c8fa5ede41/sse,https://mcp.api-inference.modelscope.net/fa7ca04baeee46/sse,https://mcp.api-inference.modelscope.net/ad865130580a46/sse,http://127.0.0.1:8000/sse".split(",", -1);

        for (String mcpServerUrl : mcpServerUrlArr) {
            try {
                McpTool tool = new McpTool();
                String listResult = tool.listTool(mcpServerUrl);
                McpToolResponse mcpToolResponse = JSON.parseObject(listResult, McpToolResponse.class);
                for (ToolInfo toolInfo : mcpToolResponse.getData()) {
                    System.out.printf("%s : %s %n", toolInfo.getName(), toolInfo.getDescription());
                }
            } catch (Exception e) {
                log.error("call mcp tool error: ", e);
            }
        }
    }

    @Test
    public void callTimeMcpToolTest() {
        String mcpServerUrl = "https://mcp.api-inference.modelscope.net/1784ac5c6d0044/sse";
        McpTool tool = new McpTool();
        Map<String, String> input = new HashMap<>();
        input.put("timezone", "America/New_York");
        String callResult = tool.callTool(mcpServerUrl, "get-current-date", input);
        String prettyJson = JSON.toJSONString(JSON.parseObject(callResult), SerializerFeature.PrettyFormat);
        log.info("call tool result： \n {}", prettyJson);
    }

    @Test
    public void callDeepWikiFetchMcpToolTest() {
        String mcpServerUrl = "https://mcp.api-inference.modelscope.net/e1cf0a85d1a942/sse";
        McpTool tool = new McpTool();
        Map<String, String> input = new HashMap<>();
        input.put("url", "https://deepwiki.com/jd-opensource/joyagent-jdgenie");
        String callResult = tool.callTool(mcpServerUrl, "deepwiki_fetch", input);
        String prettyJson = JSON.toJSONString(JSON.parseObject(callResult), SerializerFeature.PrettyFormat);
        log.info("call tool result： \n {}", prettyJson);
    }

    @Test
    public void callFetchUrlMcpToolTest() {
        String mcpServerUrl = "https://mcp.api-inference.modelscope.net/8434c8fa5ede41/sse";
        McpTool tool = new McpTool();
        Map<String, String> input = new HashMap<>();

        input.put("url", "https://baijiahao.baidu.com/s?id=1843925725610800319");
//        fetch_html : Fetch a website and return the content as HTML
//        fetch_markdown : Fetch a website and return the content as Markdown
//        fetch_txt : Fetch a website, return the content as plain text (no HTML)
//        fetch_json : Fetch a JSON file from a URL
        String callResult = tool.callTool(mcpServerUrl, "fetch_txt", input);
        String prettyJson = JSON.toJSONString(JSON.parseObject(callResult), SerializerFeature.PrettyFormat);
        log.info("call tool result： \n {}", prettyJson);
    }

    @Test
    public void callMcpToolForFundTest() {
        String mcpServerUrl = "https://mcp.api-inference.modelscope.net/cb3b13f09a684f/sse";
        String callResult = "";
        String prettyJson = "";
        McpTool tool = new McpTool();
        Map<String, Object> input = new HashMap<>();
//        input.put("kw", "机器人");
//        input.put("pageNum", 1);
//        input.put("pageSize", 10);
//        String callResult = tool.callTool(mcpServerUrl, "fund.knoewledge", input);
//        String prettyJson = JSON.toJSONString(JSON.parseObject(callResult), SerializerFeature.PrettyFormat);
//        log.info("call tool result： \n {}", prettyJson);

        input.put("count", 10);
        input.put("input", "019305");
        callResult = tool.callTool(mcpServerUrl, "fund.stock_search", input);
        prettyJson = JSON.toJSONString(JSON.parseObject(callResult), SerializerFeature.PrettyFormat);
        log.info("call tool result： \n {}", prettyJson);
    }

    @Test
    public void callReportToolTest() {
        GenieConfig genieConfig = SpringContextHolder.getApplicationContext().getBean(GenieConfig.class);
        String query = "今天北京天气怎么样？，最后以 markdown展示最终结果";
        ReportTool reportTool = new ReportTool();
        AgentContext agentContext = BaseUtils.getAgentContext(query, genieConfig);
        ToolCollection builtToolCollection = BaseUtils.buildToolCollection(agentContext, genieConfig);
        agentContext.setToolCollection(builtToolCollection);
        reportTool.setAgentContext(agentContext);
        Map<String, String> toolInput = new HashMap<>();
        toolInput.put("fileName", "北京今天天气情况.md");
        toolInput.put("fileType", "markdown");
        toolInput.put("fileDescription", "北京今日天气简报，内容包括天气描述、气温、湿度和风速等关键信息。");
        toolInput.put("task", "请以简明扼要的方式，用Markdown格式总结今天北京的天气，包括：天气状况（如晴、阴、雨等）、当前气温、湿度、风速等。强调信息完整明确，适合作为一页快速参考。");
        Object result = reportTool.execute(toolInput);
        if (Objects.nonNull(result)) {
            System.out.println((String) result);
        }
    }


}
