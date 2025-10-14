package org.mmmq.core;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.*;

public class Subscriber {

    private final String name;
    private final Host host;
    private final Set<String> subscribed = new HashSet<>();
    private final Queue<Message> messageQueue = new LinkedList<>();

    public Subscriber(String name, String host) {
        try {
            this.name = name;
            this.host = new Host(Inet4Address.getByName(host));
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid host: " + host, e);
        }
    }

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Subscriber that)) {
            return false;
        }
        return Objects.equals(name, that.name) && Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, host);
    }
}
