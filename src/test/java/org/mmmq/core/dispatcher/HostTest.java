package org.mmmq.core.dispatcher;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.InetAddress;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HostTest {

    @Test
    @DisplayName("Host 생성 시 호스트 연결 검증을 진행한다.")
    void convertAddressWhenCreateTest() {
        assertThatCode(() ->  new Host("http", "localhost", 8080) {
            @Override
            public boolean healthCheck(InetAddress host) {
                return true;
            }
        }).doesNotThrowAnyException();

        assertThatThrownBy(() ->  new Host("http", "localhost", 8080) {
            @Override
            public boolean healthCheck(InetAddress host) {
                return false;
            }
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
