package com.jd.genie.sse.v2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jd.genie.model.req.GptQueryReq;
import com.jd.genie.sse.v2.response.ResponseWrapper;
import org.springframework.http.MediaType;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SseClient {
    private final String serverUrl;
    private final ExecutorService executorService;

    public SseClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void sendRequest(GptQueryReq request) {
        executorService.submit(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(serverUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", MediaType.TEXT_EVENT_STREAM_VALUE);
                connection.setDoOutput(true);
                connection.setConnectTimeout(30000);
                connection.setReadTimeout(1800000);

                // 打印请求信息
                System.out.println("\n=== 发送请求 ===");
                System.out.println("URL: " + serverUrl);
                System.out.println("请求数据: " + JSON.toJSONString(request));
                System.out.println("================");

                // 发送请求
                String jsonRequest = JSON.toJSONString(request);
                connection.getOutputStream().write(jsonRequest.getBytes(StandardCharsets.UTF_8));

                // 处理响应
                System.out.println("\n=== 服务器响应 ===");
                try (InputStream inputStream = connection.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // 直接打印每一行内容
                        // System.out.println(line);
                        line = line.replace("data:", "");
                        if (line.length() != 0) {
                            ResponseWrapper responseWrapper = JSON.parseObject(line, ResponseWrapper.class);
                            if ("text".equals(responseWrapper.getResponseType())
                                    && "success".equals(responseWrapper.getStatus())) {
                                System.out.println(responseWrapper.getResponseAll());
                            }
                        }
//                        if (line.length() != 0) {
//                            JSONObject jsonObject = JSON.parseObject(line);
//                            String prettyJson = JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat);
//                            System.out.println(prettyJson);
//                        }
                    }
                }
                System.out.println("================");
            } catch (Exception e) {
                System.err.println("Error in SSE connection: " + e.getMessage());
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    private void handleJsonResponse(JSONObject json) {
        // 处理不同类型的响应
        if (json.containsKey("type")) {
            String type = json.getString("type");
            switch (type) {
                case "content":
                    System.out.println("\n【内容响应】");
                    System.out.println(json.getString("content"));
                    break;
                case "status":
                    System.out.println("\n【状态更新】");
                    System.out.println("状态: " + json.getString("status"));
                    if (json.containsKey("message")) {
                        System.out.println("消息: " + json.getString("message"));
                    }
                    break;
                case "error":
                    System.err.println("\n【错误响应】");
                    System.err.println("错误码: " + json.getString("code"));
                    System.err.println("错误信息: " + json.getString("message"));
                    break;
                case "stream":
                    boolean isStreaming = json.getBooleanValue("streaming");
                    if (isStreaming) {
                        System.out.println("\n【开始流式响应】");
                    } else {
                        System.out.println("\n【流式响应结束】");
                    }
                    break;
                default:
                    System.out.println("\n【其他响应】");
                    System.out.println(json.toJSONString());
            }
        } else if (json.containsKey("content")) {
            // 处理没有type字段但有content的情况
            System.out.print(json.getString("content"));
            System.out.flush();
        } else {
            // 处理其他JSON格式
            System.out.println("\n【JSON响应】");
            System.out.println(json.toJSONString());
        }
    }

    private void handleRawData(String data) {
        // 处理非JSON格式的原始数据
        if (data != null && !data.trim().isEmpty()) {
            System.out.print(data);
            System.out.flush();
        }
    }

    public void close() {
        executorService.shutdown();
    }

}
