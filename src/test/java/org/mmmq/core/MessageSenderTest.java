package org.mmmq.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mmmq.config.TestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static org.mmmq.core.HostTest.LOCALHOST;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestConfiguration.class
)
class MessageSenderTest {

    @Autowired
    RestClient mockServerClient;

    @Autowired
    MockRestServiceServer server;

    @Test
    @DisplayName("특정 host에게 메시지를 전달할 수 있다.")
    void sendMessageTest() {
        MessageSender messageSender = new MessageSender(mockServerClient);
        Host host = LOCALHOST;

        server.expect(ExpectedCount.once(), requestTo(MessageSender.convertToUri(LOCALHOST.address, "/messages")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess());

        messageSender.send(host, new Message("topic", Map.of("key", "value")));

        server.verify();
    }

}
