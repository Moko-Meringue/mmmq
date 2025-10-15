package org.mmmq.core.template;

public record TopicSubscriptionTemplate(
        String name,
        String topic
) {

    public TopicSubscriptionTemplate {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (topic == null) {
            throw new IllegalArgumentException("topic is null");
        }
    }
}
