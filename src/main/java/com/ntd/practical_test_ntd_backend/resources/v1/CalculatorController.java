package com.ntd.practical_test_ntd_backend.resources.v1;

import java.math.BigDecimal;

import com.ntd.practical_test_ntd_backend.exception.InsufficientBalance;
import com.ntd.practical_test_ntd_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ntd.practical_test_ntd_backend.utils.AuthUtils;
import com.ntd.practical_test_ntd_backend.dto.CalculatorDTO;
import com.ntd.practical_test_ntd_backend.services.CalculatorService;

@RestController
@CrossOrigin
public class CalculatorController {
    @Autowired
    private CalculatorService calculatorService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/v1/calculator/add", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity Add(@RequestBody CalculatorDTO request)
    {
        try {
            Long userId = AuthUtils.getLoggedUserId(userService);
            BigDecimal result = calculatorService.Addition(userId, request.getNumber1(), request.getNumber2());
            return ResponseEntity.ok(result);

        } catch (InsufficientBalance e) {
            return ResponseEntity.badRequest().body("Insufficient balance on account!");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @RequestMapping(value = "/v1/calculator/subtract", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity Subtract(@RequestBody CalculatorDTO request)
    {
        try {
            Long userId = AuthUtils.getLoggedUserId(userService);
            BigDecimal result = calculatorService.Subtraction(userId, request.getNumber1(), request.getNumber2());
            return ResponseEntity.ok(result);

        } catch (InsufficientBalance e) {
            return ResponseEntity.badRequest().body("Insufficient balance on account!");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @RequestMapping(value = "/v1/calculator/divide", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity Divide(@RequestBody CalculatorDTO request)
    {
        try {
            Long userId = AuthUtils.getLoggedUserId(userService);
            BigDecimal result = calculatorService.Division(userId, request.getNumber1(), request.getNumber2());
            return ResponseEntity.ok(result);

        } catch (InsufficientBalance e) {
            return ResponseEntity.badRequest().body("Insufficient balance on account!");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @RequestMapping(value = "/v1/calculator/multiply", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity Multiply(@RequestBody CalculatorDTO request)
    {
        try {
            Long userId = AuthUtils.getLoggedUserId(userService);
            BigDecimal result = calculatorService.Multiplication(userId, request.getNumber1(), request.getNumber2());
            return ResponseEntity.ok(result);

        } catch (InsufficientBalance e) {
            return ResponseEntity.badRequest().body("Insufficient balance on account!");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @RequestMapping(value = "/v1/calculator/squareroot", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity SquareRoot(@RequestBody CalculatorDTO request)
    {
        try {
            Long userId = AuthUtils.getLoggedUserId(userService);
            BigDecimal result = calculatorService.SquareRoot(userId, request.getNumber1());
            return ResponseEntity.ok(result);

        } catch (InsufficientBalance e) {
            return ResponseEntity.badRequest().body("Insufficient balance on account!");
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
