package com.ntd.practical_test_ntd_backend.dto;

import java.math.BigDecimal;

import com.ntd.practical_test_ntd_backend.enums.OperationTypesEnum;

public class CalculatorDTO {
    private BigDecimal number1;
    private BigDecimal number2;
    private OperationTypesEnum operationType;

    //Constructor
    public CalculatorDTO(BigDecimal number1, BigDecimal number2, OperationTypesEnum operationType) {
        this.number1 = number1;
        this.number2 = number2;
        this.operationType = operationType;
    }

    // Getters and Setters
    public BigDecimal getNumber1() {
        return number1;
    }
    public void setNumber1(BigDecimal number1) {
        this.number1 = number1;
    }
    public BigDecimal getNumber2() {
        return number2;
    }
    public void setNumber2(BigDecimal number2) {
        this.number2 = number2;
    }
    public OperationTypesEnum getOperationType() {
        return operationType;
    }
    public void setOperationType(OperationTypesEnum operationType) {
        this.operationType = operationType;
    }
}
