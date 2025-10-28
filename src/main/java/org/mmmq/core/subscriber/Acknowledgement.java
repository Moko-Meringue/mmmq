package org.mmmq.core.subscriber;

public enum Acknowledgement {
    ACK("ACK"),
    NAK("NAK"),
    ;

    private final String code;

    Acknowledgement(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
