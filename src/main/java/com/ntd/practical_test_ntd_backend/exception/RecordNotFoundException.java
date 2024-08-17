package com.ntd.practical_test_ntd_backend.exception;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException() {super("Record not found!");}
}
