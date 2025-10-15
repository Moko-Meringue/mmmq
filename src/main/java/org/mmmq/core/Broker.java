package org.mmmq.core;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
class Broker {

    final List<Subscriber> subscribers = new ArrayList<>();

    void push(Message message) {
        subscribers.stream()
                .filter(subscriber -> subscriber.isSubscribing(message.topic()))
                .forEach(subscriber -> subscriber.push(message));
    }

    void link(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    void unlink(String name) {
        Subscriber subscriber = findSubscriber(name);
        subscribers.remove(subscriber);
    }

    Subscriber findSubscriber(String name) {
        return subscribers.stream()
                .filter(sub -> Objects.equals(sub.getName(), name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No such subscriber with name: " + name));
    }
}
