package org.mmmq.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ConsumerTest {

    static InetAddress LOCALHOST;

    @BeforeAll
    static void init() throws UnknownHostException {
        LOCALHOST = InetAddress.getLocalHost();
    }

    @Test
    @DisplayName("Consumer를 생성할 수 있다.")
    void newConsumerTest() throws IOException {
        Consumer consumer = new Consumer(LOCALHOST);
    }

    @Test
    @DisplayName("Host 헬스체크를 할 수 있어야 한다.")
    void healthCheckTest() throws IOException {
        Consumer consumer = new Consumer(LOCALHOST);

        assertThat(consumer.healthCheck()).isTrue();
    }

    @Test
    @DisplayName("생성 시 호스트 연결 검증을 진행한다.")
    void validateHostConnectionInCreationTest() {
        assertThatThrownBy(() -> new Consumer(InetAddress.getByName("0.0.0.1")))
                .isInstanceOf(IOException.class);
    }
}
