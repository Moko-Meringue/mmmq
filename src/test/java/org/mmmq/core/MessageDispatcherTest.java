package org.mmmq.core;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mmmq.config.TestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mmmq.core.HostTest.LOCALHOST;

@Disabled
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestConfiguration.class
)
class MessageDispatcherTest {

    @Test
    @DisplayName("MessageDispatcher는 Thread를 실행하여 모든 Subscriber의 큐를 확인하고, 큐에 메시지가 존재하면 poll해 Host에게 전달한다.")
    void dispatchTest() throws Exception {

        Subscriber subscriber = new Subscriber("name", LOCALHOST);
        Message message = new Message("topic", Map.of());

        MessageDispatcher dispatcher = new MessageDispatcher(
                null
        );

//        dispatcher.addSubscriber(subscriber);

        // 어떻게 테스트할 수 있을까
//        dispatcher.start();

        subscriber.messageQueue.add(message);
        Thread.sleep(100);

        assertThat(subscriber.messageQueue).isEmpty();
    }
}
