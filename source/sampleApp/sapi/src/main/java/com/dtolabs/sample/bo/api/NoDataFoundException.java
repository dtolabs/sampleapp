package com.dtolabs.sample.bo.api;

public class NoDataFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NoDataFoundException() {

    }

    public NoDataFoundException(String message) {
        super(message);

    }

    public NoDataFoundException(String message, Throwable cause) {
        super(message, cause);

    }

    public NoDataFoundException(Throwable cause) {
        super(cause);

    }

}
