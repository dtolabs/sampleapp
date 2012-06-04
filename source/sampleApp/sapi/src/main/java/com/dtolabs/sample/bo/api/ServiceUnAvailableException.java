package com.dtolabs.sample.bo.api;

public class ServiceUnAvailableException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ServiceUnAvailableException() {

    }

    public ServiceUnAvailableException(String message) {
        super(message);

    }

    public ServiceUnAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceUnAvailableException(Throwable cause) {
        super(cause);

    }

}
