package com.ntd.practical_test_ntd_backend.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {super(String.format("User {0} not found!", username));}
}
