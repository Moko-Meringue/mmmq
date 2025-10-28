package org.mmmq.core.subscriber;

import java.net.InetAddress;
import java.net.URI;

import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

public class MessageSenderFactory {

    public static MessageSender create(Host host) {
        return new MessageSender(createRestClient(host));
    }

    private static RestClient createRestClient(Host host) {
        return RestClient.builder()
            .baseUrl(convertToUri(host.address))
            .defaultStatusHandler(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                (request, response) -> {
                    throw new MessageDeliveryException(
                        "Failed to send message to subscriber: " + response.getStatusCode().value(),
                        response.getStatusCode().value()
                    );
                }
            )
            .build();
    }

    private static URI convertToUri(InetAddress address) {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(address.getHostAddress())
            .build()
            .toUri();
    }
}
