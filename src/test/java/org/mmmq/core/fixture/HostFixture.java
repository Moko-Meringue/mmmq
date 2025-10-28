package org.mmmq.core.fixture;

import java.net.InetAddress;

import org.mmmq.core.subscriber.Host;

public class HostFixture {

    public static Host localhost() {
        try {
            return new Host(InetAddress.getLocalHost()) {
                @Override
                public boolean healthCheck(InetAddress host) {
                    return true;
                }
            };
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
