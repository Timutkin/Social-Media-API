package ru.timutkin.socialmediaapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations= "classpath:application-test.yaml")
@SpringBootTest
class SocialMediaApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
