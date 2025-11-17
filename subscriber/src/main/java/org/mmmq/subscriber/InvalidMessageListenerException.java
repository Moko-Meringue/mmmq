package org.mmmq.subscriber;

public class InvalidMessageListenerException extends RuntimeException {

    public InvalidMessageListenerException(String message) {
        super(message);
    }
}
