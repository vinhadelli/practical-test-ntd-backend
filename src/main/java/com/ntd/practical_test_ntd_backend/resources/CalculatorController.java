package com.ntd.practical_test_ntd_backend.resources;

import org.springframework.beans.factory.annotation.Autowired;
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
    public void Add(CalculatorDTO request)
    {
        Long userId = AuthUtils.getLoggedUserId();
        calculatorService.Addition(userId, request.getNumber1(), request.getNumber2());
    }
    @RequestMapping(value = "/calculator/subtract", method = RequestMethod.POST, produces="application/json")
    public void Subtract(CalculatorDTO request)
    {
        Long userId = AuthUtils.getLoggedUserId();
        calculatorService.Subtraction(userId, request.getNumber1(), request.getNumber2());
    }
    @RequestMapping(value = "/calculator/divide", method = RequestMethod.POST, produces="application/json")
    public void Divide(CalculatorDTO request)
    {
        Long userId = AuthUtils.getLoggedUserId();
        calculatorService.Division(userId, request.getNumber1(), request.getNumber2());
    }
    @RequestMapping(value = "/calculator/multiply", method = RequestMethod.POST, produces="application/json")
    public void Multiply(CalculatorDTO request)
    {
        Long userId = AuthUtils.getLoggedUserId();
        calculatorService.Multiplication(userId, request.getNumber1(), request.getNumber2());
    }
    @RequestMapping(value = "/calculator/squareroot", method = RequestMethod.POST, produces="application/json")
    public void SquareRoot(CalculatorDTO request)
    {
        Long userId = AuthUtils.getLoggedUserId();
        calculatorService.SquareRoot(userId, request.getNumber1());
    }
}
