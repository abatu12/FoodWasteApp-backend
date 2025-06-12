package com.example.foodwasteapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {
                "spring.security.enabled=false"
        }
)
class FoodWasteAppApplicationTests {
    @Test
    void contextLoads() {
    }
}
