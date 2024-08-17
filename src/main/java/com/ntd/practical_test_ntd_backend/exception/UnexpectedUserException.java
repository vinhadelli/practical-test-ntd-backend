package com.ntd.practical_test_ntd_backend.exception;

public class UnexpectedUserException extends RuntimeException {
    public UnexpectedUserException(){super("The User received is not the expected one!");}
}
