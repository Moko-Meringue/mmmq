package org.mmmq.core.subscriber;

import org.mmmq.core.subscriber.response.Acknowledgement;

public record MessageReceivedResponse(
        Acknowledgement acknowledgement
) {

}

