package com.ntd.practical_test_ntd_backend.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ntd.practical_test_ntd_backend.auth.AuthUtils;
import com.ntd.practical_test_ntd_backend.dto.TokenDTO;
import com.ntd.practical_test_ntd_backend.dto.UserDTO;
import com.ntd.practical_test_ntd_backend.services.UserService;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired

    @RequestMapping(value = "/auth/sigup", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity CreateUser(UserDTO user)
    {
        try {
            userService.createUser(user);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(true);
    }

    @PostMapping("/auth/login")
    public ResponseEntity login(@RequestBody UserDTO loginUserDto) 
    {
        TokenDTO token;
        try {
            token = userService.login(loginUserDto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
        return ResponseEntity.ok(token);
    }

    @GetMapping("/user/balance")
    public Double GetUserBalance()
    {
        Long userId = AuthUtils.getLoggedUserId();
        return userService.getUserBalance(userId);
    }
}
