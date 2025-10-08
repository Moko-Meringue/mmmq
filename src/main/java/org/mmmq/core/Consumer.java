package org.mmmq.core;

import java.io.IOException;
import java.net.InetAddress;

public class Consumer {

    private final InetAddress host;

    public Consumer(InetAddress host) throws IOException {
        validateHost(host);
        this.host = host;
    }

    private void validateHost(InetAddress host) throws IOException {
        if (!healthCheck(host)) {
            throw new IOException("Host is not reachable: " + host.getHostAddress());
        }
    }

    boolean healthCheck() {
        try {
            return host.isReachable(5000);
        } catch (Exception ignored) {
            return false;
        }
    }

    boolean healthCheck(InetAddress host) {
        try {
            return host.isReachable(5000);
        } catch (Exception ignored) {
            return false;
        }
    }
}
