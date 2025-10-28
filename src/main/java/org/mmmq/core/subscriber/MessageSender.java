package org.mmmq.core.subscriber;

import org.mmmq.core.message.Message;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

public class MessageSender {

    final RestClient restClient;

    public MessageSender(RestClient restClient) {
        this.restClient = restClient;
    }

    public SubscriberResponse send(Message message) {
        return restClient.post()
            .uri("/messages")
            .contentType(MediaType.APPLICATION_JSON)
            .body(message)
            .retrieve()
            .toEntity(SubscriberResponse.class)
            .getBody();
    }
}
