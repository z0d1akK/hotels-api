package com.zodiakk.hotelsapi.common.exception;

public class InvalidTimeZoneException extends RuntimeException {

    public InvalidTimeZoneException(String message) {
        super(message);
    }

    public InvalidTimeZoneException(String message, Throwable cause) {
        super(message, cause);
    }
}
