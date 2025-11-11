package org.mmmq.gateway;

import org.mmmq.core.message.Message;
import org.mmmq.gateway.broker.Broker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MMMQGateway {

    final Broker broker;

    public MMMQGateway(Broker broker) {
        this.broker = broker;
    }

    @PostMapping("/messages")
    public ResponseEntity<Void> postMessage(@RequestBody Message message) {
        broker.push(message);
        return ResponseEntity.noContent().build();
    }
}
