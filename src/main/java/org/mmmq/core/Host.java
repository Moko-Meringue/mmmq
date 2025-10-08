package org.mmmq.core;

import java.io.IOException;
import java.net.InetAddress;

public class Host {

    private final InetAddress address;

    public Host(InetAddress address) throws IOException {
        validateHost(address);
        this.address = address;
    }

    private void validateHost(InetAddress host) throws IOException {
        if (!healthCheck(host)) {
            throw new IOException("Host is not reachable: " + host.getHostAddress());
        }
    }

    boolean healthCheck() {
        try {
            return address.isReachable(5000);
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
