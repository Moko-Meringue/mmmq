package org.mmmq.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mmmq.config.TestConfiguration;
import org.mmmq.core.template.SubscriberRegistrationTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mmmq.core.HostTest.LOCALHOST;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = TestConfiguration.class
)
public class ManagerTest {

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Manager manager;

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
        Manager manager = new Manager(broker);

        manager.postMessage(message);

        verify(broker).push(message);
    }

    @Test
    @DisplayName("Manager는 외부로부터 Subscriber를 등록할 수 있다.")
    void registerSubscriberTest() throws JsonProcessingException {
        String name = "new subscriber";
        Host host = LOCALHOST;
        Broker broker = manager.broker;
        Subscriber subscriber = new Subscriber(name, LOCALHOST);

        SubscriberRegistrationTemplate template = new SubscriberRegistrationTemplate(
                name,
                host.address.getHostAddress()
        );

        RestAssured.given().log().all()
                .body(objectMapper.writeValueAsString(template))
                .contentType("application/json")
                .when().log().all()
                .post("/subscribers")
                .then().log().all()
                .statusCode(200);


        assertThat(broker.subscribers).contains(subscriber);
    }

//
//    @Test
//    @DisplayName("Manager는 외부로부터 Subscriber를 삭제할 수 있다.")
//    void test() {
//
//    }
}
