package org.mmmq.core;

import org.mmmq.core.subscriber.MessageReceivedResponse;
import org.mmmq.core.subscriber.response.Acknowledgement;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.InetAddress;
import java.net.URI;
import java.util.Optional;

@Component
public class MessageSender {

    final RestClient restClient;

    public MessageSender(RestClient restClient) {
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

    public MessageReceivedResponse send(Host host, Message message) {
        ResponseEntity<ClientResponse> response = restClient.post()
                .uri(convertToUri(host.address, "/messages"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(message)
                .retrieve()
                .toEntity(ClientResponse.class);

        return Optional.ofNullable(response.getBody())
                .map(clientResponse -> new MessageReceivedResponse(clientResponse.acknowledgement()))
                .orElse(new MessageReceivedResponse(Acknowledgement.NAK));
    }

    public record ClientResponse(
            Acknowledgement acknowledgement
    ) {
    }
}
