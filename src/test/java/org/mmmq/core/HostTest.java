package org.mmmq.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HostTest {

    public static Host LOCALHOST;

    static {
        try {
            LOCALHOST = new Host(InetAddress.getLocalHost());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Host를 생성할 수 있다.")
    void newHostTest() {
        Host host = LOCALHOST;
    }

    @Test
    @DisplayName("Host 헬스체크를 할 수 있어야 한다.")
    void healthCheckTest() {
        Host host = LOCALHOST;

        assertThat(host.healthCheck()).isTrue();
    }

    @Test
    @DisplayName("생성 시 호스트 연결 검증을 진행한다.")
    void validateHostConnectionInCreationTest() {
        assertThatThrownBy(() -> new Host(InetAddress.getByName("0.0.0.1")))
                .isInstanceOf(IOException.class);
    }
}
