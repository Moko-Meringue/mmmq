package org.mmmq.core;

import java.util.*;

public class Subscriber {

    private final String name;
    private final Host host;
    private final Set<String> subscribed = new HashSet<>();
    private final Queue<Message> messageQueue = new LinkedList<>();

    public Subscriber(String name, Host host) {
        this.name = name;
        this.host = host;
    }

    public void push(Message message) {
        messageQueue.add(message);
    }

    Queue<Message> getMessageQueue() {
        return messageQueue;
    }

    public String getName() {
        return name;
    }

    public void subscribe(String topic) {
        subscribed.add(topic);
    }

    public boolean isSubscribing(String topic) {
        return subscribed.contains(topic);
    }
}
