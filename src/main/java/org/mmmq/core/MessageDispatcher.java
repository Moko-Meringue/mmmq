package org.mmmq.core;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class MessageDispatcher {

    final Thread messageChecker;
    final MessageSender messageSender;
    final CircularList<Subscriber> subscribers = new CircularList<>();
    final Logger log = LoggerFactory.getLogger(MessageDispatcher.class);
    final ThreadPoolExecutor executorService = new ThreadPoolExecutor(
            10,
            30,
            40L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100)
    );

    public MessageDispatcher(MessageSender messageSender) {
        this.messageSender = messageSender;
        this.messageChecker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    if (executorService.getQueue().size() < 100) {
                        Subscriber subscriber = subscribers.getNext();
                        Optional.ofNullable(subscriber.poll())
                                .ifPresent(message -> executorService.submit(() -> {
                                    messageSender.send(subscriber.host, message);
                                }));
                    }
                } catch (Exception e) {
                    log.warn("Failed to dispatch message: {}", e.getMessage());
                }
            }
        });
        //this.messageChecker = new Thread(() -> {
        //    while (true) {
        //        try {
        //            Subscriber subscriber = subscribers.getNext();
        //            Optional<Message> messageOpt = Optional.ofNullable(subscriber.poll());
        //
        //            if (messageOpt.isPresent()) {
        //                // 메시지가 있다면 ExecutorService에 제출
        //                try {
        //                    executorService.submit(() -> {
        //                        messageSender.send(subscriber.host, messageOpt.get());
        //                    });
        //                } catch (RejectedExecutionException ree) {
        //                    // 💡 [핵심] Executor 거부 시 메시지 유실 방지 및 잠시 대기
        //                    subscriber.retry(messageOpt.get()); // 메시지 재삽입 (이전 답변 참고)
        //                    log.warn("Executor full. Pausing checker for 100ms.");
        //                    Thread.sleep(100); // 💡 과부하 시 잠시 대기
        //                }
        //            } else {
        //                // 💡 [핵심] 메시지를 가져오지 못했을 때(Idle)도 CPU 낭비 방지
        //                Thread.sleep(10);
        //            }
        //        } catch (InterruptedException ie) {
        //            Thread.currentThread().interrupt();
        //            log.info("Message checker thread stopped.");
        //            break;
        //        } catch (Exception e) {
        //            log.error("Dispatch failure: {}", e.getMessage(), e);
        //            try { Thread.sleep(1000); } catch (InterruptedException ignored) {} // 예상치 못한 오류 시 긴 대기
        //        }
        //    }
        //});
    }

    @PostConstruct
    void start() {
        messageChecker.start();
    }

    public class CircularList<T> {

        private Node<T> head;
        private Node<T> tail;
        private Node<T> current;
        private int size;

        public CircularList() {
            this.head = null;
            this.tail = null;
            this.current = null;
            this.size = 0;
        }

        public void add(T data) {
            Node<T> newNode = new Node<>(data);
            if (head == null) {
                head = newNode;
                tail = newNode;
                newNode.next = head;
            } else {
                tail.next = newNode;
                tail = newNode;
                tail.next = head;
            }
            size++;
        }

        public T getNext() {
            if (current == null) {
                current = head;
            } else {
                current = current.next;
            }
            return current.data;
        }

        public int size() {
            return size;
        }

        private static class Node<T> {
            T data;
            Node<T> next;

            Node(T data) {
                this.data = data;
            }
        }
    }
}
