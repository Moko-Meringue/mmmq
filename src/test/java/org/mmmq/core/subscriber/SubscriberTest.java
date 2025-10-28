package org.mmmq.core.subscriber;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mmmq.core.fixture.HostFixture;
import org.mmmq.core.message.Message;
import org.mmmq.core.message.Topic;

class SubscriberTest {

    Subscriber subscriber;

    @BeforeEach
    void setUp() {
        subscriber = new Subscriber.Builder("name", HostFixture.localhost()).build();
    }

    @Test
    @DisplayName("push 테스트")
    void pushTest() {
        Message message = new Message("test", Map.of("key", "value"));
        subscriber.push(message);

        assertThat(subscriber.messageQueue).contains(message);
    }

    @Test
    @DisplayName("push 동시성 보장 테스")
    void pushConcurrencyTest() throws InterruptedException {
        int threadCount = 100;
        int messagesPerThread = 10;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int threadId = i;
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int j = 0; j < messagesPerThread; j++) {
                        Message message = new Message("topic", Map.of("id", threadId, "msg", j));
                        subscriber.push(message);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }
        
        startLatch.countDown();
        endLatch.await();
        executor.shutdown();

        int expectedCount = threadCount * messagesPerThread;
        int actualCount = subscriber.messageQueue.size();

        assertThat(actualCount).isEqualTo(expectedCount);
    }

    @Test
    @DisplayName("isSubscribing 테스트")
    void isSubscribingTest() {
        subscriber.topics.addAll(
            Set.of(
                new Topic("topic1"),
                new Topic("topic2")
            )
        );

        assertThat(subscriber.isSubscribing("topic1")).isTrue();
        assertThat(subscriber.isSubscribing("topic2")).isTrue();
        assertThat(subscriber.isSubscribing("topic3")).isFalse();
    }
}
