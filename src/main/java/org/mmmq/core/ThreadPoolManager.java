package org.mmmq.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ThreadPoolManager {

    @Autowired
    RestTemplate restTemplate;

//    CircularList<Subscriber> subscriberList = new CircularList<>();
//    ThreadPoolExecutor threadPoolExecutor;
//    MessageSender messageSender;
//    Thread thread = new Thread(() -> {
//        CompletableFuture
//        try {
//            while (true) {
//                Subscriber subscriber = subscriberList.getNext();
//                if (subscriber.hasMessage()) {
//                    threadPoolExecutor.submit(() -> {
//                        Message message = subscriber.poll();
//                        messageSender.send(message);
//                    });
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        });
//
//    @PostConstruct
//    void go() {
//        thread.start();
//    }
//
//    @Scheduled
//    void healthCheck() {
//        if (!thread.isAlive()) {
//            handleDead();
//        }
//    }
//
//    void handleDead() {
//        thread.start();
//    }
}
