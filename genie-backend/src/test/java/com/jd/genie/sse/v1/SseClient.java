package com.jd.genie.sse.v1;


import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SseClient {
    private final String serverUrl;

    public SseClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void connect(GptQueryReq request, SseEventHandler handler) {
        try {
            URL url = new URL(serverUrl + "/web/api/v1/gpt/queryAgentStreamIncr");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "text/event-stream");
            connection.setDoOutput(true);

            // 发送请求
            String jsonRequest = JSON.toJSONString(request);
            connection.getOutputStream().write(jsonRequest.getBytes(StandardCharsets.UTF_8));
            System.out.println("Response Code: " + connection.getResponseCode());
            // 处理响应
            if (connection.getResponseCode() == 200) {
                try (InputStream inputStream = connection.getInputStream();
                     BufferedReader reader = new BufferedReader(
                         new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                    StringBuilder eventData = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("data: ")) {

                            // 处理数据行
                            String data = line.substring(6);
                            eventData.append(data).append("\n");
                        } else if (line.startsWith("event: ")) {
                            // 处理事件类型
                            String eventType = line.substring(7);
                            handler.onEventType(eventType);
                        } else if (line.startsWith("id: ")) {
                            // 处理消息ID
                            String messageId = line.substring(4);
                            handler.onMessageId(messageId);
                        } else if (line.isEmpty()) {
                            // 空行表示消息结束
                            if (eventData.length() > 0) {
                                handler.onMessage(eventData.toString());
                                eventData.setLength(0);
                            }
                            handler.onEvent();
                        }
                    }

                    // 处理最后一条消息（如果没有以空行结尾）
                    if (eventData.length() > 0) {
                        handler.onMessage(eventData.toString());
                    }
                }
            }
            System.out.println("Response Message: " + connection.getResponseMessage());
        } catch (Exception e) {
            handler.onError(e);
        }
    }

    public interface SseEventHandler {
        void onMessage(String message);
        void onEvent();
        void onEventType(String eventType);
        void onMessageId(String messageId);
        void onError(Throwable t);
    }
}
