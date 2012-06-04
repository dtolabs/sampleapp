package com.dtolabs.sample.bo.api;

/**
 * @author sharad
 * 
 */

public class InvalidArgumentException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
 * 
 */
    public InvalidArgumentException() {

    }

    /**
     * 
     * @param message
     */
    public InvalidArgumentException(String message) {
        super(message);

    }

    /**
     * @param message
     * @param cause
     */
    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);

    }

    /**
     * 
     * @param cause
     */
    public InvalidArgumentException(Throwable cause) {
        super(cause);

    }

}
