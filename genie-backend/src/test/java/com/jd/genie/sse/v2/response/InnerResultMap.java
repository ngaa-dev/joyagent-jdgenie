package com.jd.genie.sse.v2.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InnerResultMap {
    private int agentType;
    private String taskSummary;
}
