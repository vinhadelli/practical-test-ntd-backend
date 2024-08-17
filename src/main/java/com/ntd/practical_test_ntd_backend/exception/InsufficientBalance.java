package com.ntd.practical_test_ntd_backend.exception;

public class InsufficientBalance extends RuntimeException {
    public InsufficientBalance() {super("The User does not have enough credits for the operation!");}
}
