package com.ntd.practical_test_ntd_backend.services.interfaces;

import java.math.BigDecimal;

public interface ICalculatorService {
    BigDecimal Addition (Long userId, BigDecimal a, BigDecimal b);
    BigDecimal Subtraction (Long userId, BigDecimal a, BigDecimal b);
    BigDecimal Division (Long userId, BigDecimal a, BigDecimal b);
    BigDecimal Multiplication (Long userId, BigDecimal a, BigDecimal b);
    BigDecimal SquareRoot (Long userId, BigDecimal a);
}
