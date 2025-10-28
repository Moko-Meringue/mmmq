package org.mmmq.core.subscriber;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.mmmq.core.message.Message;
import org.mmmq.core.message.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Subscriber {

    private static final Logger log = LoggerFactory.getLogger(Subscriber.class);

    final String name;
    final Host host;
    final Set<Topic> topics;
    final LinkedBlockingQueue<Message> messageQueue;
    final MessageSender messageSender;
    final ThreadPoolExecutor threadPoolExecutor;
    final Thread thread;

    public Subscriber(
        String name,
        Host host,
        Set<Topic> topics,
        LinkedBlockingQueue<Message> messageQueue,
        ThreadPoolExecutor threadPoolExecutor
    ) {
        this.name = name;
        this.host = host;
        this.topics = topics;
        this.messageQueue = messageQueue;
        this.threadPoolExecutor = threadPoolExecutor;
        this.messageSender = MessageSenderFactory.create(host);
        thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (threadPoolExecutor.isShutdown()) {
                        break;
                    }
                    Message message = messageQueue.take();
                    threadPoolExecutor.submit(() -> {
                        messageSender.send(message);
                    });
                } catch (Exception e) {
                    log.warn("Failed to send message: {}", e.getMessage());
                }
            }
        });
    }

    public void push(Message message) {
        messageQueue.add(message);
    }

    public boolean isSubscribing(String topic) {
        return topics.contains(new Topic(topic));
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

    public static class Builder {

        private static final ThreadPoolExecutor DEFAULT_THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            2,
            5,
            40L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(5)
        );

        static {
            DEFAULT_THREAD_POOL_EXECUTOR.allowCoreThreadTimeOut(true);
        }

        private String name;
        private Host host;
        private Set<Topic> subscribed = new HashSet<>();
        private ThreadPoolExecutor threadPoolExecutor = DEFAULT_THREAD_POOL_EXECUTOR;

        public Builder(String name, Host host) {
            this.name = name;
            this.host = host;
        }

        public Builder subscribes(String... topics) {
            for (String topic : topics) {
                this.subscribed.add(new Topic(topic));
            }
            return this;
        }

        public Builder threadPoolExecutor(ThreadPoolExecutor executor) {
            this.threadPoolExecutor = executor;
            return this;
        }

        public Subscriber build() {
            return new Subscriber(
                name,
                host,
                subscribed,
                new LinkedBlockingQueue<>(),
                threadPoolExecutor
            );
        }
    }
}
