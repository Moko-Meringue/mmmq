package org.mmmq.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageTest {

    @Test
    @DisplayName("메시지는 Host에게 전달할 데이터를 보관한다.")
    void hasDataTest() {
        Message message = new Message("moko", Map.of("key1", "value"));

        assertThat(message.content())
                .containsExactlyEntriesOf(Map.of("key1", "value"));
    }
}
