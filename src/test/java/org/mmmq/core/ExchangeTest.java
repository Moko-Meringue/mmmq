package org.mmmq.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mmmq.core.ConsumerTest.LOCALHOST;

public class ExchangeTest {

    @Test
    @DisplayName("Exchange를 생성할 수 있다.")
    void newExchangeTest() {
        Exchange exchange = new Exchange("name");
    }

    @Test
    @DisplayName("Exchange에는 하나 이상의 Consumer를 등록할 수 있다.")
    void addConsumerTest() throws IOException {
        Exchange exchange = new Exchange("name");
        Consumer consumer1 = new Consumer(LOCALHOST);
        Consumer consumer2 = new Consumer(LOCALHOST);

        exchange.addConsumer(consumer1);
        exchange.addConsumer(consumer2);
        assertThat(exchange.getConsumerSize()).isEqualTo(2);
    }
}
