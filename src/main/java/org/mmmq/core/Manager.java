package org.mmmq.core;

import org.mmmq.core.template.SubscriberRegistrationTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Manager {

    final Broker broker;

    public Manager(Broker broker) {
        this.broker = broker;
    }

    @PostMapping("/messages")
    public ResponseEntity<Void> postMessage(@RequestBody Message message) {
        broker.push(message);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/subscribers")
    public ResponseEntity<String> postSubscriber(@RequestBody SubscriberRegistrationTemplate template) {
        Subscriber subscriber = new Subscriber(template.name(), template.host());
        broker.link(subscriber);
        return ResponseEntity.ok(subscriber.getName());
    }
}
