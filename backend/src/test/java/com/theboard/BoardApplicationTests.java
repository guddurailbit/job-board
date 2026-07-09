package com.theboard;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BoardApplicationTests {

    @Test
    void contextLoads() {
        // If the Spring context fails to start, this test fails —
        // a quick smoke test that wiring (controllers, repositories, config) is correct.
    }
}
