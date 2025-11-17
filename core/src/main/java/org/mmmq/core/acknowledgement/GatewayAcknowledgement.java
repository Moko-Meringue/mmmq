package org.mmmq.core.acknowledgement;

public record GatewayAcknowledgement(
        Acknowledgement acknowledgement
) {

    public boolean isAck() {
        return this.acknowledgement == Acknowledgement.ACK;
    }
}
