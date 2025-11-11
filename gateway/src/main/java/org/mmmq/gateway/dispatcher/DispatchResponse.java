package org.mmmq.gateway.dispatcher;

import org.mmmq.core.message.Acknowledgement;

public record DispatchResponse(
        Acknowledgement acknowledgement
) {

    public boolean isAck() {
        return this.acknowledgement == Acknowledgement.ACK;
    }
}
