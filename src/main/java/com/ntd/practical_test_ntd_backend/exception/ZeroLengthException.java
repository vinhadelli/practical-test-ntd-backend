package com.ntd.practical_test_ntd_backend.exception;

public class ZeroLengthException extends RuntimeException {
    public ZeroLengthException() {
        super("Is not possible to generate a string with zero length!");
    }
}
