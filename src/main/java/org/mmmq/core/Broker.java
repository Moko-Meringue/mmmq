package org.mmmq.core;

import java.util.ArrayList;
import java.util.List;

public class Broker {

    private final List<Subscriber> subscribers = new ArrayList<>();

    public void push(Message message) {
        subscribers.stream()
                .filter(subscriber -> subscriber.isSubscribing(message.getTopic()))
                .forEach(subscriber -> subscriber.push(message));
    }

    void link(Subscriber subscriber) {
        subscribers.add(subscriber);
    }
}
