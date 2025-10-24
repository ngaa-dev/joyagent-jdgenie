package com.jd.genie.sse.v2;

import com.jd.genie.model.req.GptQueryReq;

import java.util.UUID;

public class SseClientMain {
    public static void main(String[] args) {
        String serverUrl = "http://localhost:8080/web/api/v1/gpt/queryAgentStreamIncr";
        String user = "SseClientMain";
        String outputStyle = "docs";

        SseClient client = new SseClient(serverUrl);

        // 创建请求对象
        GptQueryReq request = new GptQueryReq();
//        request.setQuery("查询今天是几月几号？星期几，推荐下今天吃什么，以及做法？");
        request.setQuery("今天北京天气怎么样？");
        request.setUser(user);
        request.setRequestId(UUID.randomUUID().toString());
        request.setSessionId(String.valueOf(System.currentTimeMillis()));
        request.setOutputStyle(outputStyle);

        // 发送请求
        client.sendRequest(request);

        // 添加关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down SSE client...");
            client.close();
        }));
    }
}