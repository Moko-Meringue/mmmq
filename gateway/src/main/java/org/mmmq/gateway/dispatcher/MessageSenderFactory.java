package org.mmmq.gateway.dispatcher;

import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

public class MessageSenderFactory {

    public static MessageSender create(Host host) {
        return new MessageSender(createRestClient(host));
    }

    private static RestClient createRestClient(Host host) {
        return RestClient.builder()
                .baseUrl(convertToUri(host))
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

    private static String convertToUri(Host host) {
        return UriComponentsBuilder.newInstance()
                .scheme(host.protocol.getScheme())
                .host(host.address.getHostAddress())
                .port(host.port)
                .build()
                .toUri()
                .toString();
    }
}
