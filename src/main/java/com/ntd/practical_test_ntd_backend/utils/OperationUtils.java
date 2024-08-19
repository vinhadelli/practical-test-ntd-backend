package com.ntd.practical_test_ntd_backend.utils;

public class OperationUtils {
    /**
     * Static Utils function to get the String name of the operation
     * @param type
     * @return
     */
    public static String getNameOfOperation(int type)
    {
        return switch (type) {
            case 1 -> "Addition";
            case 2 -> "Subtraction";
            case 3 -> "Multiplication";
            case 4 -> "Division";
            case 5 -> "Square Root";
            case 6 -> "String Generation";
            case 7 -> "Added Credits";
            default -> "";
        };
    }
}
