package com.jd.genie.sse.v2.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventData {
    private int messageOrder;
    private String messageType;
    private ResultMapInfo resultMap;
    private String messageId;
    private String taskId;
    private int taskOrder;
}