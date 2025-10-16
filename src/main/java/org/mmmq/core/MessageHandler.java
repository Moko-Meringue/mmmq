package org.mmmq.core;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.InetAddress;
import java.net.URI;

@Component
public class MessageHandler {

    final RestClient restClient;

    public MessageHandler(RestClient restClient) {
        this.restClient = restClient;
    }

    public static URI convertToUri(InetAddress address, String path) {
        String host = address.getHostAddress();
        String uriString = String.format("https://%s%s", host, path);

        return URI.create(uriString);
    }

    public void handle(Host host, Message message) {
        restClient.post()
                .uri(convertToUri(host.address, "/messages"))
                .body(message)
                .retrieve()
                .body(Void.class);
    }
}
