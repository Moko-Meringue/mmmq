package org.mmmq.core.broker;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mmmq.core.message.Message;
import org.mmmq.core.subscriber.Subscriber;
import org.mockito.Mockito;

class BrokerTest {

    @Test
    @DisplayName("Broker는 메시지를 받으면 적절한 Subscriber에 전달할 수 있다.")
    void forwardMessageTest() {
        Subscriber subscriber = Mockito.mock(Subscriber.class);
        Broker broker = new Broker(List.of(subscriber));
        when(subscriber.isSubscribing("topic1")).thenReturn(true);

        Message message = new Message("topic1", Map.of("key1", "value"));
        broker.push(message);

        verify(subscriber).push(message);
    }
}
