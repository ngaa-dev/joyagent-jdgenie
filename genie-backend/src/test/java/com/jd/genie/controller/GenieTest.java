package com.jd.genie.controller;

import com.jd.genie.agent.util.SpringContextHolder;
import com.jd.genie.config.GenieConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
public class GenieTest {
    @Test
    public void serverPortTest() {
        GenieConfig genieConfig = SpringContextHolder.getApplicationContext().getBean(GenieConfig.class);
        assertEquals(8080, (int) genieConfig.getServerPort());
        assertEquals(true, genieConfig.getEnableLiteLLMProxy());
    }
}
