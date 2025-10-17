package org.mmmq.core;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetAddress;
import java.net.URI;

@Component
public class MessageHandler {

    final RestClient restClient;

    public MessageHandler(RestClient restClient) {
        this.restClient = restClient;
    }

    public static URI convertToUri(InetAddress address, String path) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host(address.getHostAddress())
                .path(path)
                .build()
                .toUri();
    }

    public void handle(Host host, Message message) {
        restClient.post()
                .uri(convertToUri(host.address, "/messages"))
                .body(message)
                .retrieve()
                .body(Void.class);
    }
}
