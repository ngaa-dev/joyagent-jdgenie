package com.jd.genie.medical;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OkHttpExample {
    public static void main(String[] args) {
        // 1. 创建 OkHttpClient 实例
        OkHttpClient client = new OkHttpClient();

        // 2. 创建 Request 对象
        Request request = new Request.Builder()
                .url("https://www.dayi.org.cn/search?keyword=%E8%84%91%E6%A2%97%E5%A1%9E")
                .build();

        // 3. 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 获取响应体的字符串
            assert response.body() != null;
            String responseData = response.body().string();
            System.out.println(responseData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

