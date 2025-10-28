package org.mmmq.core.subscriber;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HostTest {

    @Test
    @DisplayName("Host 생성 시 호스트 연결 검증을 진행한다.")
    void validateHostWhenCreateTest() {
        assertThatCode(() ->  new Host(InetAddress.getLocalHost()) {
            @Override
            public boolean healthCheck(InetAddress host) {
                return true;
            }
        }).doesNotThrowAnyException();

        assertThatThrownBy(() ->  new Host(InetAddress.getLocalHost()) {
            @Override
            public boolean healthCheck(InetAddress host) {
                return false;
            }
        }).isInstanceOf(IOException.class);
    }
}
