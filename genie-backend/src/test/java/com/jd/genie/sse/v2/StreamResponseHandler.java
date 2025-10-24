package com.jd.genie.sse.v2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class StreamResponseHandler {
    private StringBuilder buffer = new StringBuilder();
    private boolean isProcessingStream = false;

    public void processData(String data) {
        try {
            // 跳过心跳和空数据
            if (data == null || data.trim().isEmpty() || "heartbeat".equals(data)) {
                return;
            }

            // 尝试解析为 JSON
            JSONObject json = JSON.parseObject(data);

            // 处理流式响应标记
            if (json.containsKey("stream")) {
                boolean isStream = json.getBooleanValue("stream");
                if (isStream) {
                    isProcessingStream = true;
                    System.out.println("\n=== 开始流式响应 ===");
                } else {
                    if (isProcessingStream) {
                        // 处理缓冲区中剩余的数据
                        processBufferedData();
                        System.out.println("\n=== 流式响应结束 ===");
                    }
                    isProcessingStream = false;
                    buffer.setLength(0);
                }
            }

            // 处理流式数据
            if (isProcessingStream) {
                handleStreamData(json);
            } else {
                // 处理普通响应
                handleNormalResponse(json);
            }

        } catch (Exception e) {
            // 如果 JSON 解析失败，可能是流式数据的片段
            if (isProcessingStream) {
                buffer.append(data);
                // 尝试处理缓冲区中的数据
                processBufferedData();
            } else {
                System.err.println("\n解析数据时出错: " + e.getMessage());
                System.err.println("原始数据: " + data);
            }
        }
    }

    private void handleStreamData(JSONObject json) {
        // 处理流式数据的不同类型
        if (json.containsKey("content")) {
            String content = json.getString("content");
            System.out.print(content);  // 不换行打印，实现流式效果
            System.out.flush();        // 立即刷新输出缓冲区
        }

        if (json.containsKey("status")) {
            System.out.println("\n[状态] " + json.getString("status"));
        }

        if (json.containsKey("progress")) {
            System.out.println("\n[进度] " + json.getString("progress"));
        }
    }

    private void handleNormalResponse(JSONObject json) {
        System.out.println("\n=== 普通响应 ===");
        if (json.containsKey("content")) {
            System.out.println("内容: " + json.getString("content"));
        }
        if (json.containsKey("status")) {
            System.out.println("状态: " + json.getString("status"));
        }
        if (json.containsKey("message")) {
            System.out.println("消息: " + json.getString("message"));
        }
    }

    private void processBufferedData() {
        if (buffer.length() > 0) {
            String bufferedData = buffer.toString();
            try {
                // 尝试解析缓冲区中的数据
                JSONObject json = JSON.parseObject(bufferedData);
                handleStreamData(json);
            } catch (Exception e) {
                // 如果解析失败，直接打印原始数据
                System.out.print(bufferedData);
                System.out.flush();
            }
            buffer.setLength(0);
        }
    }
}