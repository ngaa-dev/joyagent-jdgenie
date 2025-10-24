package com.jd.genie.sse.v2.response;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper {
    private int useTokens;
    private boolean finished;
    private String packageType;
    private String reqId;
    private String responseType;
    private ResultMapInfo resultMap;
    private boolean encrypted;
    private String response;
    private String responseAll;
    private String status;
    private int useTimes;

    public static void main(String[] args) {
        String jsonString = "{\"useTokens\":0,\"finished\":true,\"packageType\":\"result\",\"reqId\":\"genie1758181162664:e88f5059-9c0c-4bfd-ab88-efa9108ffc41\",\"responseType\":\"text\",\"resultMap\":{\"multiAgent\":{},\"agentType\":\"5\",\"eventData\":{\"messageOrder\":1,\"messageType\":\"task\",\"resultMap\":{\"result\":\"今天是2025年9月18日。\",\"messageTime\":\"1758181184696\",\"messageType\":\"result\",\"resultMap\":{\"agentType\":5,\"taskSummary\":\"今天是2025年9月18日。\"},\"requestId\":\"genie1758181162664:e88f5059-9c0c-4bfd-ab88-efa9108ffc41\",\"messageId\":\"bbf516f5-6bdf-42bc-928f-1d4b61c48335\",\"finish\":true,\"isFinal\":true},\"messageId\":\"bbf516f5-6bdf-42bc-928f-1d4b61c48335\",\"taskId\":\"ecf21110-91c9-46fa-b381-624e24c063ea\",\"taskOrder\":15}},\"encrypted\":false,\"response\":\"今天是2025年9月18日。\",\"responseAll\":\"今天是2025年9月18日。\",\"status\":\"success\",\"useTimes\":0}";
        ResponseWrapper responseWrapper = JSON.parseObject(jsonString, ResponseWrapper.class);
        Assertions.assertEquals("今天是2025年9月18日。", responseWrapper.getResponseAll());
        Assertions.assertEquals("text", responseWrapper.getResponseType());

        jsonString = "{\"useTokens\":0,\"finished\":false,\"packageType\":\"heartbeat\",\"reqId\":\"genie1758181162664:e88f5059-9c0c-4bfd-ab88-efa9108ffc41\",\"responseType\":\"text\",\"encrypted\":false,\"response\":\"\",\"responseAll\":\"\",\"status\":\"success\",\"useTimes\":0}";
        responseWrapper = JSON.parseObject(jsonString, ResponseWrapper.class);
        Assertions.assertEquals("heartbeat", responseWrapper.getPackageType());
        Assertions.assertEquals("text", responseWrapper.getResponseType());
    }

}
