package org.mmmq.core.subscriber;

public class MessageDeliveryException extends RuntimeException {
    
    private final int statusCode;
    
    public MessageDeliveryException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public MessageDeliveryException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
}