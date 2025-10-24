package com.jd.genie.sse.v1;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.UUID;

public class SseClientExample {
    public static void main(String[] args) {
        SseClient client = new SseClient("http://localhost:8080");

        GptQueryReq request = new GptQueryReq();
        request.setQuery("查询今日黄金最新价格");
        String uniqueRequestId = UUID.randomUUID().toString();
        request.setRequestId(uniqueRequestId);
//        request.setIsStream(true);

        SseClient.SseEventHandler handler = new SseClient.SseEventHandler() {
            @Override
            public void onMessage(String message) {
                try {
                    // 尝试解析JSON消息
                    JSONObject jsonMessage = JSON.parseObject(message);
                    System.out.println("收到JSON消息: " + jsonMessage.toJSONString());
                } catch (Exception e) {
                    // 如果不是JSON，直接打印原始消息
                    System.out.println("收到原始消息: " + message);
                }
            }

            @Override
            public void onEvent() {
                System.out.println("收到事件分隔符");
            }

            @Override
            public void onEventType(String eventType) {
                System.out.println("事件类型: " + eventType);
            }

            @Override
            public void onMessageId(String messageId) {
                System.out.println("消息ID: " + messageId);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("发生错误: " + t.getMessage());
                t.printStackTrace();
            }
        };

        client.connect(request, handler);
    }
}
