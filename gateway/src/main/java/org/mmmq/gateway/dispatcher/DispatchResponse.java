package org.mmmq.gateway.dispatcher;

import org.mmmq.core.acknowledgement.Acknowledgement;

public record DispatchResponse(
        Acknowledgement acknowledgement
) {

    public boolean isAck() {
        return this.acknowledgement == Acknowledgement.ACK;
    }
}
