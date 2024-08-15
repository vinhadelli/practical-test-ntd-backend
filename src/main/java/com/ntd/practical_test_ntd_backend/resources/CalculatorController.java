package com.ntd.practical_test_ntd_backend.resources;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ntd.practical_test_ntd_backend.auth.AuthUtils;
import com.ntd.practical_test_ntd_backend.dto.CalculatorDTO;
import com.ntd.practical_test_ntd_backend.services.CalculatorService;

@RestController
public class CalculatorController {
    @Autowired
    private CalculatorService calculatorService;

    @RequestMapping(value = "/calculator/add", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity Add(CalculatorDTO request)
    {
        try {
            Long userId = AuthUtils.getLoggedUserId();
            BigDecimal result = calculatorService.Addition(userId, request.getNumber1(), request.getNumber2());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @RequestMapping(value = "/calculator/subtract", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity Subtract(CalculatorDTO request)
    {
        try {
            Long userId = AuthUtils.getLoggedUserId();
            BigDecimal result = calculatorService.Subtraction(userId, request.getNumber1(), request.getNumber2());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @RequestMapping(value = "/calculator/divide", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity Divide(CalculatorDTO request)
    {
        try {
            Long userId = AuthUtils.getLoggedUserId();
            BigDecimal result = calculatorService.Division(userId, request.getNumber1(), request.getNumber2());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @RequestMapping(value = "/calculator/multiply", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity Multiply(CalculatorDTO request)
    {
        try {
            Long userId = AuthUtils.getLoggedUserId();
            BigDecimal result = calculatorService.Multiplication(userId, request.getNumber1(), request.getNumber2());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @RequestMapping(value = "/calculator/squareroot", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity SquareRoot(CalculatorDTO request)
    {
        try {
            Long userId = AuthUtils.getLoggedUserId();
            BigDecimal result = calculatorService.SquareRoot(userId, request.getNumber1());
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
