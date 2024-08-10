package com.ntd.practical_test_ntd_backend.enums;

public enum OperationTypesEnum {
    ADDITION(1),
    SUBTRACTION(2),
    MULTIPLICATION(3),
    DIVISION(4),
    SQUARE_ROOT(5),
    GENERATE_STRING(6),
    ADD_CREDITS(7);

    /**
     * Value for this OperationTypesEnum
     */
    public final int Value;
 
    private OperationTypesEnum(int value)
    {
        Value = value;
    }
}
