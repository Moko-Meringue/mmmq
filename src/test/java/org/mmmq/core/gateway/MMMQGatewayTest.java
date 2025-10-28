package org.mmmq.core.gateway;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mmmq.core.TestConfiguration;
import org.mmmq.core.broker.Broker;
import org.mmmq.core.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = TestConfiguration.class
)
class MMMQGatewayTest {

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Manager는 외부로부터 메시지를 받을 수 있다.")
    void receiveMessageTest() throws JsonProcessingException {
        Message message = new Message("topic", Map.of("key", "value"));
        RestAssured.given().log().all()
            .body(objectMapper.writeValueAsString(message))
            .contentType("application/json")
            .when().log().all()
            .post("/messages")
            .then().log().all()
            .statusCode(204);
    }

    @Test
    @DisplayName("Manager는 전달받은 메시지를 브로커에게 전달할 수 있다.")
    void forwardToBrokerTest() {
        Message message = new Message("topic", Map.of("key", "value"));
        Broker broker = mock(Broker.class);
        MMMQGateway MMMQGateway = new MMMQGateway(broker);

        MMMQGateway.postMessage(message);

        verify(broker).push(message);
    }
}
