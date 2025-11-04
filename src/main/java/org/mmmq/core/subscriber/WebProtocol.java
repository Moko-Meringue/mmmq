package org.mmmq.core.subscriber;

import java.util.Arrays;

public enum WebProtocol {

    HTTP("http"),
    HTTPS("https")
    ;
    
    private final String scheme;

    WebProtocol(String scheme) {
        this.scheme = scheme;
    }

    public static WebProtocol fromScheme(String scheme) {
        return Arrays.stream(values())
                .filter(protocol -> protocol.getScheme().equals(scheme))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported scheme: " + scheme));
    }
    
    public String getScheme() {
        return scheme;
    }
}
