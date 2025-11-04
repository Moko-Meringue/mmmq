package org.mmmq.core.dispatcher;

public record DispatchResponse(
        Acknowledgement acknowledgement
) {

    public boolean isAck() {
        return this.acknowledgement == Acknowledgement.ACK;
    }
}
