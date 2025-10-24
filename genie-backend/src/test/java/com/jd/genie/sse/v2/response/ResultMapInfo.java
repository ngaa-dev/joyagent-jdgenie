package com.jd.genie.sse.v2.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultMapInfo {
    private String result;
    private String messageTime;
    private String messageType;
    private InnerResultMap resultMap;
    private String requestId;
    private String messageId;
    private boolean finish;
    private boolean isFinal;
}


