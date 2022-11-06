package com.senstile.receiveordersystem.exception;

public class ProviderDeliveryException extends RuntimeException {

    public ProviderDeliveryException() {
    }

    public ProviderDeliveryException(String message) {
        super(message);
    }

    public ProviderDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
