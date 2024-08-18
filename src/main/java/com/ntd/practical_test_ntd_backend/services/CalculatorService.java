package com.ntd.practical_test_ntd_backend.services;

import java.math.BigDecimal;
import java.math.MathContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ntd.practical_test_ntd_backend.entities.Operation;
import com.ntd.practical_test_ntd_backend.entities.Record;
import com.ntd.practical_test_ntd_backend.entities.User;
import com.ntd.practical_test_ntd_backend.enums.OperationTypesEnum;
import com.ntd.practical_test_ntd_backend.exception.DividedByZeroException;
import com.ntd.practical_test_ntd_backend.exception.InsufficientBalance;
import com.ntd.practical_test_ntd_backend.exception.NegativeSquareRootException;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IOperationRepository;
import com.ntd.practical_test_ntd_backend.persistence.interfaces.IRecordRepository;

@Service
public class CalculatorService {
    @Autowired
    private UserService userService;
    @Autowired
    private IRecordRepository recordRepository;
    @Autowired
    private IOperationRepository operationRepository;

    // Function to add two numbers and subtract the cost of the operation from the received user balance.
    // Parameter: {userId} - The ID of the User requesting the operation.
    // Parameter: {a} - First number of the operation.
    // Parameter: {b} - Second number of the operation.
    // Returns: BigDecimal - Result of the Operation.
    @Transactional
    public BigDecimal Addition (Long userId, BigDecimal a, BigDecimal b)
    {
        BigDecimal result = a.add(b);
        ProcessOperation(OperationTypesEnum.ADDITION, userId, String.format("SUM: %g = %g + %g", result, a, b));
        return result;
    }
    // Function to Subtract two numbers and subtract the cost of the operation from the received user balance.
    // Parameter: {userId} - The ID of the User requesting the operation.
    // Parameter: {a} - First number of the operation.
    // Parameter: {b} - Second number of the operation.
    // Returns: BigDecimal - Result of the Operation.
    @Transactional
    public BigDecimal Subtraction (Long userId, BigDecimal a, BigDecimal b)
    {
        BigDecimal result = a.subtract(b);
        ProcessOperation(OperationTypesEnum.SUBTRACTION, userId, String.format("SUB: %g = %g - %g", result, a, b));
        return result;
    }
    //Function to Divide two numbers and subtract the cost of the operation from the received user balance.
    // Parameter: {userId} - The ID of the User requesting the operation.
    // Parameter: {a} - First number of the operation.
    // Parameter: {b} - Second number of the operation.
    // Returns: BigDecimal - Result of the Operation.
    @Transactional
    public BigDecimal Division (Long userId, BigDecimal a, BigDecimal b)
    {
        if(b.compareTo(BigDecimal.ZERO) == 0)
            throw new DividedByZeroException();
        BigDecimal result = a.divide(b);
        ProcessOperation(OperationTypesEnum.DIVISION, userId, String.format("DIV: %g = %g / %g", result, a, b));
        return result;
    }
    //Function to Multiply two numbers and subtract the cost of the operation from the received user balance.
    // Parameter: {userId} - The ID of the User requesting the operation.
    // Parameter: {a} - First number of the operation.
    // Parameter: {b} - Second number of the operation.
    // Returns: BigDecimal - Result of the Operation.
    @Transactional
    public BigDecimal Multiplication (Long userId, BigDecimal a, BigDecimal b)
    {
        BigDecimal result = a.multiply(b);
        ProcessOperation(OperationTypesEnum.MULTIPLICATION, userId, String.format("MULT: %g = %g * %g", result, a, b));
        return result;
    }
    // Function to take the SquareRoot of a number and subtract the cost of the operation from the received user balance,
    // Parameter: {userId} - The ID of the User requesting the operation.
    // Parameter: {a} - Number of the operation.
    // Returns: BigDecimal - Result of the Operation.
    @Transactional
    public BigDecimal SquareRoot (Long userId, BigDecimal a)
    {
        if(a.compareTo(BigDecimal.ZERO) < 0)
            throw new NegativeSquareRootException();
        BigDecimal result = a.sqrt(new MathContext(5));
        ProcessOperation(OperationTypesEnum.SQUARE_ROOT, userId, String.format("SQROT: %g = √%g", result, a));
        return result;
    }

    // This function processes the operation received. It goes to the database to retrieve the correct type of operation
    // and user. It also gets the remaining balance of the user and subtracts for the cost of the operation.
    // Parameter: {type} - Type of operation.
    // Parameter: {userId} - The ID of the User requesting the operation.
    // Parameter: {message} - Message to be stored in the record. Contains the result and the parameters.
    private void ProcessOperation(OperationTypesEnum type, Long userId, String message)
    {
        Operation operation = operationRepository.findByType(type.Value);
        User user = userService.getUser(userId);
        Double remainingBalance = userService.getUserBalance(userId) - operation.getCost();
        if(remainingBalance < 0)
            throw new InsufficientBalance();
        recordRepository.save(new Record(operation, user, operation.getCost(), remainingBalance, message));
    }
}
