package org.mmmq.core.subscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.net.InetAddress;
import java.net.URI;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mmmq.core.fixture.HostFixture;
import org.mmmq.core.fixture.MockRestServiceServerFixture;
import org.mmmq.core.message.Message;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class MessageSenderTest {

    RestClient restClient;
    MockRestServiceServer server;
    ObjectMapper objectMapper;
    Host host;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        host = HostFixture.localhost();
        RestClient tempClient = RestClient.builder()
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
        MockRestServiceServerFixture serverFixture = MockRestServiceServerFixture.create(tempClient);
        restClient = serverFixture.getRestClient();
        server = serverFixture.getMockServer();
    }

    @Test
    @DisplayName("특정 host에게 메시지를 전달할 수 있다.")
    void sendMessageTest() {
        MessageSender messageSender = new MessageSender(restClient);
        Host host = HostFixture.localhost();

        server.expect(ExpectedCount.once(), requestTo(convertToUri(host.address) + "/messages"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(withSuccess());

        messageSender.send(new Message("topic", Map.of("key", "value")));

        server.verify();
    }

    @Test
    @DisplayName("MessageSender는 메시지를 전달한 후, Host로부터 응답(ACK/NAK)을 받을 수 있다.")
    void receiveResponseTest() throws JsonProcessingException {
        MessageSender messageSender = new MessageSender(restClient);

        server.expect(ExpectedCount.once(), requestTo(convertToUri(host.address) + "/messages"))
            .andExpect(method(HttpMethod.POST))
            .andRespond(
                withSuccess()
                    .body(objectMapper.writeValueAsBytes(new SubscriberResponse(Acknowledgement.ACK)))
                    .contentType(MediaType.APPLICATION_JSON)
            );

        SubscriberResponse response = messageSender.send(new Message("topic", Map.of("key", "value")));

        assertThat(response.acknowledgement()).isEqualTo(Acknowledgement.ACK);
    }

    private static URI convertToUri(InetAddress address) {
        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host(address.getHostAddress())
            .build()
            .toUri();
    }
}
