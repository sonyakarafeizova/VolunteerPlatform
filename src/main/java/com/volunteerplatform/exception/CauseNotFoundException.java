package com.volunteerplatform.exception;

public class CauseNotFoundException extends RuntimeException {
    public CauseNotFoundException(String message){
        super(message);
    }
}
