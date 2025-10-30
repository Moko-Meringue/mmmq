package org.mmmq.core.subscriber;

public record SubscriberResponse(
        Acknowledgement acknowledgement
) {

    public boolean isAck() {
        return this.acknowledgement == Acknowledgement.ACK;
    }
}
