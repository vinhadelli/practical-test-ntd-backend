package com.ntd.practical_test_ntd_backend.services.interfaces;

import com.ntd.practical_test_ntd_backend.exception.NetworkException;

public interface IRandomService {
    String generateRandomString(int length, Long userId) throws NetworkException;
}
