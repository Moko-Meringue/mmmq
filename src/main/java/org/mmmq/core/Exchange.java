package org.mmmq.core;

import java.util.ArrayList;
import java.util.List;

public class Exchange {

    private final String name;
    private final List<Consumer> consumers;

    public Exchange(String name) {
        this.name = name;
        consumers = new ArrayList<>();
    }

    public void addConsumer(Consumer consumer) {
        consumers.add(consumer);
    }

    public int getConsumerSize() {
        return consumers.size();
    }
}
