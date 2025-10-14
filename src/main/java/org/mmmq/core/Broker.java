package org.mmmq.core;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Broker {

    final List<Subscriber> subscribers = new ArrayList<>();

    public void push(Message message) {
        subscribers.stream()
                .filter(subscriber -> subscriber.isSubscribing(message.topic()))
                .forEach(subscriber -> subscriber.push(message));
    }

    void link(Subscriber subscriber) {
        subscribers.add(subscriber);
    }
}
