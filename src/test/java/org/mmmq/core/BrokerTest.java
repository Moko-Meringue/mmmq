package org.mmmq.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mmmq.core.HostTest.LOCALHOST;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BrokerTest {

    @Test
    @DisplayName("Broker에는 Subscriber를 연결할 수 있다.")
    void addSubscriberTest() {
        Broker broker = new Broker();
        Subscriber subscriber = new Subscriber("moko", LOCALHOST);

        assertThatCode(() -> broker.link(subscriber));
    }

    @Test
    @DisplayName("Broker는 메시지를 받을 수 있다.")
    void receiveMessageTest() {
        Broker broker = new Broker();
        Subscriber subscriber = new Subscriber("moko", LOCALHOST);
        subscriber.subscribe("topic1");
        broker.link(subscriber);

        assertThatCode(() -> broker.push(new Message("topic1", Map.of("key1", "value"))))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Broker는 메시지를 받으면 적절한 Subscriber에 전달할 수 있다.")
    void forwardMessageTest() {
        Broker broker = new Broker();
        Subscriber subscriber = Mockito.mock(Subscriber.class);
        when(subscriber.isSubscribing("topic1")).thenReturn(true);
        broker.link(subscriber);

        Message message = new Message("topic1", Map.of("key1", "value"));
        broker.push(message);

        verify(subscriber).push(message);
    }
}
