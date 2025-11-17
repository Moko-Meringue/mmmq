package org.mmmq.publisher;

import org.mmmq.core.Host;
import org.mmmq.core.message.Message;

public class Publisher {

    static final int DEFAULT_MAX_RETRY_COUNT = 3;

    final Gateway gateway;
    final int maxRetryCount;

    public Publisher(Host host) {
        this.gateway = new Gateway(host);
        this.maxRetryCount = DEFAULT_MAX_RETRY_COUNT;
    }

    public Publisher(Host host, int maxRetryCount) {
        this.gateway = new Gateway(host);
        this.maxRetryCount = maxRetryCount;
    }

    public void publish(Message message) {
        try {
            for (int retryCount = 0; retryCount <= maxRetryCount; retryCount++) {
                if (gateway.send(message).isAck()) {
                    return;
                }
            }
        } catch (Exception e) {
            throw new MessagePublishException("Failed to publish message", e);
        }
    }
}
