package com.accolite.Payment.exception;

public class UserAlreadyEnrolledException extends RuntimeException {
    public UserAlreadyEnrolledException(String message){
        super(message);
    }

}