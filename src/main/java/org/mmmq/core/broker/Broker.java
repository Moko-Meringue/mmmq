package org.mmmq.core.broker;

import java.util.List;

import org.mmmq.core.message.Message;
import org.mmmq.core.subscriber.Subscriber;
import org.springframework.stereotype.Component;

@Component
public class Broker {

    final List<Subscriber> subscribers;

    public Broker(List<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

    public void push(Message message) {
        subscribers.stream()
                .filter(subscriber -> subscriber.isSubscribing(message.topic()))
                .forEach(subscriber -> subscriber.push(message));
    }
}
