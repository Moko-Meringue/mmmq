package org.mmmq.core;

import java.util.Map;

public class Message {

    private final String topic;
    private final Map<String, Object> content;

    public Message(String topic, Map<String, Object> content) {
        this.topic = topic;
        this.content = content;
    }

    public String getTopic() {
        return topic;
    }

    Map<String, Object> getContent() {
        return content;
    }
}
