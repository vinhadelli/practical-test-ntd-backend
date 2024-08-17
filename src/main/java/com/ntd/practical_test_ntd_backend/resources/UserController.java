package com.ntd.practical_test_ntd_backend.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ntd.practical_test_ntd_backend.auth.AuthUtils;
import com.ntd.practical_test_ntd_backend.dto.TokenDTO;
import com.ntd.practical_test_ntd_backend.dto.UserDTO;
import com.ntd.practical_test_ntd_backend.services.UserService;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired

    @RequestMapping(value = "/auth/signup", method = RequestMethod.POST, produces="application/json")
    public ResponseEntity CreateUser(@RequestBody UserDTO user)
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
    public ResponseEntity GetUserBalance()
    {
        try {
            Long userId = AuthUtils.getLoggedUserId(userService);
            return ResponseEntity.ok(userService.getUserBalance(userId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
