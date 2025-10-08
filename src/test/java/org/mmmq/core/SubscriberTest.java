package org.mmmq.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mmmq.core.HostTest.LOCALHOST;

public class SubscriberTest {

    @Test
    @DisplayName("Subscriber를 생성할 수 있다.")
    void newSubscriberTest() {
        assertThatCode(() -> new Subscriber("name", LOCALHOST))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Subscriber는 내부적으로 Queue를 가진다.")
    void hasQueueTest() {
        Subscriber subscriber = new Subscriber("name", LOCALHOST);
        assertThat(subscriber.getMessageQueue()).isNotNull();
        assertThat(subscriber.getMessageQueue()).isInstanceOf(Queue.class);
    }

    @Test
    @DisplayName("Subscriber는 Message를 Queue에 보관할 수 있다.")
    void hasMessageQueueTest() {
        Subscriber subscriber = new Subscriber("name", LOCALHOST);
        Message message = new Message("moko", Map.of());
        subscriber.push(message);

        assertThat(subscriber.getMessageQueue()).containsExactly(message);
    }
}
