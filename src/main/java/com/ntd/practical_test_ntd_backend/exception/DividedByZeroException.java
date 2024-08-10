package com.ntd.practical_test_ntd_backend.exception;

public class DividedByZeroException extends RuntimeException {
    public DividedByZeroException() {super("Division by zero not possible!");}
}
