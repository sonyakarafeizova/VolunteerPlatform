package com.volunteerplatform.service;

public class NoCauseFoundException extends RuntimeException {
    public NoCauseFoundException(String message) {
        super(message);
    }
}
