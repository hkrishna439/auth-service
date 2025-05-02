package com.authservice.auth_service.exceptions;

public class WrongPasswordException extends Exception {
    public WrongPasswordException(String wrongPassword) {
        super(wrongPassword)    ;
    }
}
