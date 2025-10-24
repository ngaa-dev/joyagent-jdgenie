package com.jd.genie.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class McpToolResponse {
    private String code;
    private String message;
    private List<ToolInfo> data;

}

