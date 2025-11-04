package org.mmmq.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mmmq.core.subscriber.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    classes = SubscriberConfigurationTest.SubscriberConfiguration.class
)
public class SubscriberConfigurationTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    @DisplayName("Subscriber 빈이 정상적으로 생성된다.")
    void subscriberBeanCreationTest() {
        Map<String, Subscriber> beans = applicationContext.getBeansOfType(Subscriber.class);

        assertThat(beans.keySet()).containsExactly("testSubscriber");
    }

    @Configuration
    static class SubscriberConfiguration {

        @Bean("testSubscriber")
        public Subscriber subscriber() {
            return new Subscriber.Builder("name", "http", "localhost", 8080)
                .subscribes("topic1", "topic2")
                .build();
        }
    }
}
