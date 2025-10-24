package com.jd.genie.sse.v2;

import com.alibaba.fastjson.JSONObject;

public class ResponseParser {
    public static void parseAndPrint(String response) {
        try {
            // 解析 JSON 响应
            JSONObject json = JSONObject.parseObject(response);

            // 根据实际响应结构提取数据
            if (json.containsKey("content")) {
                System.out.println("Content: " + json.getString("content"));
            }

            // 处理其他可能的响应字段
            if (json.containsKey("status")) {
                System.out.println("Status: " + json.getString("status"));
            }

        } catch (Exception e) {
            System.err.println("Error parsing response: " + e.getMessage());
        }
    }
}

