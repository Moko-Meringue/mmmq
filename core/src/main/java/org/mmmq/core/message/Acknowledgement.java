package org.mmmq.core.message;

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
