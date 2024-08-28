package com.ntd.practical_test_ntd_backend.exception;

public class NegativeLengthException extends RuntimeException {
    public NegativeLengthException() {
        super("Is not possible to generate strings with negative length!");
    }
}
